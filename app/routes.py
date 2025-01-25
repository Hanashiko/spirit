from flask import request, jsonify, Blueprint
from app import app, db, bcrypt
from app.models import User, Message
from app.encryption import generate_ecdh_keys, ecdh_shared_key, encrypt_message, decrypt_message, serialize_public_key, deserialize_public_key
from datetime import datetime
from sqlalchemy.exc import IntegrityError

bp = Blueprint('routes', __name__)

@bp.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data.get('username')
    email = data.get('email')
    password = data.get('password')

    if not username or not email or not password:
        return jsonify({'error': 'Missing required fields'}), 400

    user = User(username=username, email=email)
    user.set_password(password)

    try:
        db.session.add(user)
        db.session.commit()
    except IntegrityError:
        db.session.rollback()
        return jsonify({'error': 'Username or email already exists'}), 400

    return jsonify({'message': 'User registered successfully'}), 201

@bp.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    if not email or not password:
        return jsonify({'error': 'Missing required fields'}), 400

    user = User.query.filter_by(email=email).first()

    if user is None or not user.check_password(password):
        return jsonify({'error': 'Invalid email or password'}), 400

    private_key, public_key = generate_ecdh_keys()

    return jsonify({'message': 'Login successful', 'public_key': serialize_public_key(public_key)}), 200

@bp.route('/send_message', methods=['POST'])
def send_message():
    data = request.get_json()
    sender_id = data.get('sender_id')
    recipient_id = data.get('recipient_id')
    message_content = data.get('message_content')
    sender_private_key_pem = data.get('sender_private_key')
    recipient_public_key_pem = data.get('recipient_public_key')

    if not sender_id or not recipient_id or not message_content or not sender_private_key_pem or not recipient_public_key_pem:
        return jsonify({'error': 'Missing required fields'}), 400

    sender = User.query.get(sender_id)
    recipient = User.query.get(recipient_id)

    if sender is None or recipient is None:
        return jsonify({'error': 'Invalid sender or recipient ID'}), 400

    sender_private_key = serialization.load_pem_private_key(sender_private_key_pem.encode('utf-8'), password=None, backend=backend)
    recipient_public_key = deserialize_public_key(recipient_public_key_pem)

    shared_key = ecdh_shared_key(sender_private_key, recipient_public_key)

    encrypted_message = encrypt_message(shared_key, message_content)

    message = Message(sender_id=sender_id, recipient_id=recipient_id, content=encrypted_message, timestamp=datetime.utcnow())

    db.session.add(message)
    db.session.commit()

    return jsonify({'message': 'Message sent successfully'}), 201

app.register_blueprint(bp)