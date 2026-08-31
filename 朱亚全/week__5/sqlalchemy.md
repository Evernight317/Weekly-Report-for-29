# ORM基础知识
ORM是指对象关系映射，将数据库中的数据转换为程序中的对象，并提供方便的方法来进行数据和对象之间的转换。

ORM映射关系：
- 数据库模型映射为一个python类
- 数据库的行映射为一个实例
- 数据库的列映射为属性

# SQL alchemy的核心概念
- **engine**：建立数据库连接池，进行sql操作、事务管理等
- **session**：管理对象持久化的主要接口，提供crud等方法
- **DeclarativeBase**：定义模型基类（SQLAlchemy 2.0 新方式）
- **table**：表示数据库表的对象
- **column**：表示数据库列的对象
- **关联关系**
    - `relationship`(双向关系)
    - `ForeignKey`(单向关系)
    - 多对多关系需要中间表
#### 一对多关系（双向）
```python
class User(Base):
    __tablename__ = 'users'
    id: Mapped[int] = mapped_column(primary_key=True)
    posts: Mapped[list["Post"]] = relationship(back_populates='author')

class Post(Base):
    __tablename__ = 'posts'
    id: Mapped[int] = mapped_column(primary_key=True)
    user_id: Mapped[int] = mapped_column(ForeignKey('users.id'))
    author: Mapped["User"] = relationship(back_populates='posts')
```
#### 多对多关系
```python
# 中间表
post_tags = Table('post_tags', Base.metadata,
    Column('post_id', ForeignKey('posts.id')),
    Column('tag_id', ForeignKey('tags.id'))
)

class Post(Base):
    __tablename__ = 'posts'
    id: Mapped[int] = mapped_column(primary_key=True)
    tags: Mapped[list["Tag"]] = relationship(secondary=post_tags, back_populates='posts')

class Tag(Base):
    __tablename__ = 'tags'
    id: Mapped[int] = mapped_column(primary_key=True)
    posts: Mapped[list["Post"]] = relationship(secondary=post_tags, back_populates='posts')
```

## api
1. 创建引擎和会话：`create_engine`，`sessionmaker`
2. 定义模型类和表结构：`DeclarativeBase`，`mapped_column`，`Mapped`
3. crud操作：`session.add`，`session.execute(select())`，`session.delete`，`session.refresh`，`session.commit`
4. 查询操作：`where`，`filter_by`，`order_by`，`limit`，`join`，`group_by`，`having`
5. 关联关系：`relationship`
6. 事务管理：`session.begin`，`session.commit`，`session.rollback`

## engine：管理数据库的连接和执行sql语句
1. 创建engine：`create_engine()`
```python
from sqlalchemy import create_engine

# SQLite数据库
engine = create_engine('sqlite:///example.db')

# MySQL数据库
engine = create_engine('mysql+pymysql://user:password@localhost/dbname')

# PostgreSQL数据库
engine = create_engine('postgresql://user:password@localhost/dbname')

# 常用参数
engine = create_engine(
    'mysql+pymysql://user:password@localhost/dbname',
    echo=True,              # 打印SQL语句
    pool_size=5,            # 连接池大小
    max_overflow=10,        # 最大溢出连接数
    pool_timeout=30,        # 连接超时时间（秒）
    pool_recycle=3600       # 连接回收时间（秒）
)
```

2. 执行sql，先拿connection再execute
```python
# 使用 with 上下文管理器（推荐）
with engine.connect() as connection:
    result = connection.execute("SELECT * FROM users")
    for row in result:
        print(row)
# 退出 with 块时自动关闭连接
```

## session：管理数据库操作的会话状态
1. 创建`sessionmaker`，先创建engine再与session绑定
```python
from sqlalchemy.orm import sessionmaker

# 创建会话工厂
Session = sessionmaker(bind=engine)

# 创建会话实例
session = Session()
```

2. 增删改查的操作：先创建实例，再执行crud操作，再`session.commit`，`session.refresh`
```python
from sqlalchemy import select

# 创建（Create）
new_user = User(name='John', email='john@example.com')
session.add(new_user)
session.commit()

# 读取（Read）- SQLAlchemy 2.0 推荐方式
user = session.execute(select(User).filter_by(name='John')).scalar_one()

# 更新（Update）
user.email = 'newemail@example.com'
session.commit()

# 刷新（Refresh）- 从数据库重新加载数据
session.refresh(user)

# 删除（Delete）
session.delete(user)
session.commit()
```

3. `session.close()`
```python
# 使用完毕后关闭会话
session.close()

# 或者使用with语句自动关闭（推荐）
with Session() as session:
    user = session.execute(select(User)).scalars().first()
```

## 事务管理
```python
# 方式1：使用commit/rollback
try:
    session.add(new_user)
    session.commit()
except:
    session.rollback()
    raise

# 方式2：使用with语句（推荐）
with session.begin():
    session.add(new_user)
```

## 定义模型
1. 创建引擎engine：`create_engine()`
2. 创建模型基类：`DeclarativeBase`，所有模型必须继承它，否则就是一个普通的python类
3. 创建模型类：包含表名`__tablename__`，字段（数据类型，约束等）用`mapped_column`创建

### 常用字段类型
- `Mapped[int]`：整数
- `Mapped[str]`：字符串
- `Mapped[str | None]`：可空字符串
- `Mapped[bool]`：布尔值
- `Mapped[datetime]`：日期时间
- `Mapped[float]`：浮点数
- `Mapped[date]`：日期

### 常用约束
```python
mapped_column(primary_key=True)           # 主键
mapped_column(String(50), unique=True)    # 唯一约束
mapped_column(nullable=False)             # 非空约束
mapped_column(default=0)                  # 默认值
mapped_column(index=True)                 # 索引
mapped_column(ForeignKey('users.id'))     # 外键
```

```python
from sqlalchemy import create_engine, String, DateTime, Text
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from datetime import datetime

# 1. 创建引擎
engine = create_engine('sqlite:///example.db', echo=True)

# 2. 创建模型基类（SQLAlchemy 2.0 新方式）
class Base(DeclarativeBase):
    pass

# 3. 创建模型类
class User(Base):
    __tablename__ = 'users'  # 表名
    
    # 字段定义
    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)  # 主键
    username: Mapped[str] = mapped_column(String(50), unique=True, nullable=False)  # 唯一约束
    email: Mapped[str] = mapped_column(String(100), unique=True, nullable=False)
    age: Mapped[int] = mapped_column(default=0)  # 默认值
    bio: Mapped[str | None] = mapped_column(Text, nullable=True)  # 可空长文本
    is_active: Mapped[bool] = mapped_column(default=True)
    created_at: Mapped[datetime] = mapped_column(DateTime, default=datetime.now)  # 默认当前时间
    
    def __repr__(self):
        return f"<User(id={self.id}, username={self.username})>"

# 创建所有表
Base.metadata.create_all(engine)
```

## 查询语言
### 直接查询
- MySQL：`select * from User`
- sqlalchemy: `user = session.query(User).all()`

### 条件查询
```python
from sqlalchemy import select, or_, and_

# filter_by - 使用关键字参数（只能用于相等判断）
user = session.execute(select(User).filter_by(username='john', age=25)).scalar_one_or_none()

# where - 使用表达式（支持各种比较运算）
user = session.execute(select(User).where(User.username == 'john')).scalar_one_or_none()
users = session.execute(select(User).where(User.age > 18)).scalars().all()

# 多条件查询（AND）
users = session.execute(select(User).where(User.age == 25, User.city == 'Beijing')).scalars().all()

# OR条件
users = session.execute(select(User).where(or_(User.age == 25, User.age == 30))).scalars().all()

# 模糊查询
users = session.execute(select(User).where(User.username.like('%john%'))).scalars().all()

# IN查询
users = session.execute(select(User).where(User.id.in_([1, 2, 3]))).scalars().all()

# BETWEEN查询
users = session.execute(select(User).where(User.age.between(20, 30))).scalars().all()
```
多条件时，默认就是 and，如果是 or，就要使用`or_()`

### 排序、分页、分组、聚合查询
```python
from sqlalchemy import select, func

# 排序
# 升序（默认）
users = session.execute(select(User).order_by(User.username.asc())).scalars().all()
# 降序
users = session.execute(select(User).order_by(User.username.desc())).scalars().all()
# 多字段排序
users = session.execute(select(User).order_by(User.age.desc(), User.username.asc())).scalars().all()

# 分页查询（limit/offset)
# 限制返回行数
users = session.execute(select(User).limit(10)).scalars().all()
# 偏移（跳过前20条，取10条）
users = session.execute(select(User).offset(20).limit(10)).scalars().all()

# 分组查询
# 按年龄分组统计
result = session.execute(select(User.age, func.count(User.id)).group_by(User.age)).all()
# HAVING子句
result = session.execute(select(User.age, func.count(User.id)).group_by(User.age).having(func.count(User.id) > 5)).all()

# 聚合函数
total = session.scalar(select(func.count(User.id)))  # 计数
total_age = session.scalar(select(func.sum(User.age)))  # 求和
avg_age = session.scalar(select(func.avg(User.age)))  # 平均值
max_age = session.scalar(select(func.max(User.age)))  # 最大值
min_age = session.scalar(select(func.min(User.age)))  # 最小值

# 连接查询
# 内连接
result = session.execute(select(User, Post).join(Post, User.id == Post.user_id)).all()
# 左连接
result = session.execute(select(User, Post).outerjoin(Post, User.id == Post.user_id)).all()
```
核心：链式调用（排序、分页、条件可组合写在一起）
```python
# 实际示例
users = (session.execute(
    select(User)
    .where(User.age >= 18)
    .where(User.city == 'Beijing')
    .order_by(User.created_at.desc())
    .limit(20)
    .offset(0)
)).scalars().all()
```

注释：`all()`，`first()`，`scalar()`，`one()`，`count()`，`scalars()`
- `scalars().all()`：获取所有结果，返回列表
- `scalars().first()`：获取第一条结果，返回对象或None
- `scalar_one()`：获取唯一结果，多于一条会报错
- `scalar()`：获取标量值，返回单个值
- `count()`：获取结果数量，返回整数