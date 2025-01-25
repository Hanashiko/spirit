import os
from dotenv import load_dotenv
load_dotenv()

class Config:
    SQLALCHEMY_DATABASE_URI = os.getenv('DATABASE_URI')
    SECRET_KEY = os.getenv('SECRET_KEY')
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    DEBUG = True
    ALLOWED_HOSTS = ["*"]
    PORT = 5000
    HOST = '192.168.0.106'
    UPLOAD_FOLDER = os.path.join(os.getcwd(), 'uploads')