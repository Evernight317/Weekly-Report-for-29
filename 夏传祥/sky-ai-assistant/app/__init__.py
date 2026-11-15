"""app 包初始化：导入任何模块前先加载项目根目录的 .env"""
from pathlib import Path

from dotenv import load_dotenv

load_dotenv(Path(__file__).resolve().parent.parent / ".env")
