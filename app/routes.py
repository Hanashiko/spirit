from flask import request, jsonify, Blueprint
from app import app, db, bcrypt
from app.models import User, Message
from app.encryption import generate_ecdh_keys, ecdh_shared_key, encrypt_message, decrypt_message, serialize_public_key, deserialize_public_key
from datetime import datetime
from sqlalchemy.exc import IntegrityError
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.backends import default_backend

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
        return jsonify({'success': False, 'message': 'Missing required fields'}), 400

    user = User.query.filter_by(email=email).first()

    if user is None or not user.check_password(password):
        return jsonify({'success': False, 'message': 'Invalid email or password'}), 400

    private_key, public_key = generate_ecdh_keys()
    user.public_key = serialize_public_key(public_key)
    db.session.commit()

    return jsonify({'success': True, 'userId': user.id, 'public_key': user.public_key}), 200

# @bp.route('/send_message', methods=['POST'])
# def send_message():
#     data = request.get_json()
#     sender_id = data.get('sender_id')
#     recipient_id = data.get('recipient_id')
#     message_content = data.get('message_content')
#     sender_private_key_pem = data.get('sender_private_key')
#     recipient_public_key_pem = data.get('recipient_public_key')

#     if not sender_id or not recipient_id or not message_content or not sender_private_key_pem or not recipient_public_key_pem:
#         return jsonify({'error': 'Missing required fields'}), 400

#     sender = User.query.get(sender_id)
#     recipient = User.query.get(recipient_id)

#     if sender is None or recipient is None:
#         return jsonify({'error': 'Invalid sender or recipient ID'}), 400

#     sender_private_key = serialization.load_pem_private_key(sender_private_key_pem.encode('utf-8'), password=None, backend=backend)
#     recipient_public_key = deserialize_public_key(recipient_public_key_pem)

#     shared_key = ecdh_shared_key(sender_private_key, recipient_public_key)

#     encrypted_message = encrypt_message(shared_key, message_content)

#     message = Message(sender_id=sender_id, recipient_id=recipient_id, content=encrypted_message, timestamp=datetime.utcnow())

#     db.session.add(message)
#     db.session.commit()

#     return jsonify({'message': 'Message sent successfully'}), 201


@bp.route('/send_message', methods=['POST'])
def send_message():
    data = request.get_json()
    sender_id = data.get('sender_id')
    recipient_id = data.get('recipient_id')
    content = data.get('content')

    if not sender_id or not recipient_id or not content:
        return jsonify({'success': False, 'message': 'Missing required fields'}), 400

    sender = User.query.get(sender_id)
    receiver = User.query.get(recipient_id)

    if not sender or not receiver:
        return jsonify({'success': False, 'message': 'User not found'}), 404

    message = Message(sender_id=sender_id, recipient_id=recipient_id, content=content)
    db.session.add(message)
    db.session.commit()

    return jsonify({'success': True, 'message': 'Message sent successfully'}), 200

# @bp.route('/send_message', methods=['POST'])
# def send_message():
#     data = request.get_json()
#     sender_id = data.get('sender_id')
#     recipient_id = data.get('recipient_id')
#     content = data.get('content')
#     sender_private_key_pem = data.get('sender_private_key')
#     receiver_public_key_pem = data.get('receiver_public_key')

#     if not sender_id or not recipient_id or not content or not sender_private_key_pem or not receiver_public_key_pem:
#         return jsonify({'success': False, 'message': 'Missing required fields'}), 400

#     sender = User.query.get(sender_id)
#     receiver = User.query.get(recipient_id)

#     if not sender or not receiver:
#         return jsonify({'success': False, 'message': 'User not found'}), 404

#     # Deserialize keys
#     sender_private_key = serialization.load_pem_private_key(
#         sender_private_key_pem.encode('utf-8'),
#         password=None,
#         backend=default_backend()
#     )
#     receiver_public_key = deserialize_public_key(receiver_public_key_pem)

#     # Generate shared key
#     shared_key = ecdh_shared_key(sender_private_key, receiver_public_key)

#     # Encrypt the message
#     encrypted_message = encrypt_message(shared_key, content)

#     message = Message(sender_id=sender_id, recipient_id=recipient_id, content=encrypted_message, timestamp=datetime.utcnow())

#     db.session.add(message)
#     db.session.commit()

#     return jsonify({'success': True, 'message': 'Message sent successfully'}), 201

@bp.route('/message_history', methods=['GET'])
def message_history():
    sender_id = request.args.get('sender_id')
    recipient_id = request.args.get('recipient_id')

    if not sender_id or not recipient_id:
        return jsonify({'error': 'Missing required fields'}), 400

    messages = Message.query.filter(
        ((Message.sender_id == sender_id) & (Message.recipient_id == recipient_id)) |
        ((Message.sender_id == recipient_id) & (Message.recipient_id == sender_id))
    ).order_by(Message.timestamp).all()

    message_list = [{
        'sender_id': message.sender_id,
        'recipient_id': message.recipient_id,
        'content': message.content,
        'timestamp': message.timestamp
    } for message in messages]

    return jsonify(message_list), 200

@bp.route('/conversations', methods=['GET'])
def conversations():
    user_id = request.args.get('user_id')

    if not user_id:
        return jsonify({'error': 'Missing required fields'}), 400

    sent_messages = Message.query.filter_by(sender_id=user_id).all()
    received_messages = Message.query.filter_by(recipient_id=user_id).all()

    conversation_ids = {message.recipient_id for message in sent_messages}.union(
        {message.sender_id for message in received_messages}
    )

    conversation_users = User.query.filter(User.id.in_(conversation_ids)).all()

    user_list = [{
        'id': user.id,
        'username': user.username,
        'email': user.email,
        'full_name': user.full_name,
        'avatar_image': user.avatar_image
    } for user in conversation_users]

    return jsonify(user_list), 200

@bp.route('/update_profile', methods=['POST'])
def update_profile():
    data = request.get_json()
    user_id = data.get('user_id')
    full_name = data.get('full_name')
    phone_number = data.get('phone_number')
    about_me = data.get('about_me')
    avatar_image = data.get('avatar_image')  

    if not user_id:
        return jsonify({'error': 'Missing required fields'}), 400

    user = User.query.get(user_id)

    if user is None:
        return jsonify({'error': 'User not found'}), 404

    if full_name is not None:
        user.full_name = full_name
    if phone_number is not None:
        user.phone_number = phone_number
    if about_me is not None:
        user.about_me = about_me
    if avatar_image is not None:
        user.avatar_image = avatar_image

    db.session.commit()

    return jsonify({'message': 'Profile updated successfully'}), 200

@bp.route('/users/<int:id>/public_key', methods=['GET'])
def get_public_key(id):
    user = User.query.get(id)
    if user is None:
        return jsonify({'error': 'User not found'}), 404

    if user.public_key is None:
        return jsonify({'error': 'User does not have a public key'}), 404

    return jsonify({'public_key': user.public_key}), 200

@app.route('/users/<int:id>/update_public_key', methods=['POST'])
def update_public_key(id):
    data = request.get_json()
    public_key = data.get('public_key')

    if not public_key:
        return jsonify({'success': False, 'message': 'Missing public key'}), 400

    # Assuming you have a User model and a database session (db.session)
    user = User.query.get(id)
    if user is None:
        return jsonify({'success': False, 'message': 'User not found'}), 404

    user.public_key = public_key
    db.session.commit()

    return jsonify({'success': True, 'message': 'Public key updated successfully'}), 200


@bp.route('/updatePublicKey', methods=['POST'])
def update_public_key():
    data = request.get_json()
    user_id = data.get('user_id')
    public_key = data.get('public_key')

    if not user_id or not public_key:
        return jsonify({'success': False, 'message': 'Missing required fields'}), 400

    user = User.query.get(user_id)
    if user is None:
        return jsonify({'success': False, 'message': 'User not found'}), 404

    user.public_key = public_key
    db.session.commit()

    return jsonify({'success': True, 'message': 'Public key updated successfully'}), 200

@app.route('/user_chats/<int:user_id>', methods=['GET'])
def get_user_chats(user_id):
    chat_user_ids = db.session.query(Message.sender_id).filter(Message.recipient_id == user_id).union(
        db.session.query(Message.recipient_id).filter(Message.sender_id == user_id)
    ).distinct().all()

    chat_user_ids = [user_id[0] for user_id in chat_user_ids]

    users = User.query.filter(User.id.in_(chat_user_ids)).all()

    user_chats = [{'username': user.username, 'avatar_image': user.avatar_image.image_path if user.avatar_image else None} for user in users]

    return jsonify(user_chats)


app.register_blueprint(bp)