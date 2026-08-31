# day4

## 完成套餐管理模块所有业务功能，包括：



- 新增套餐

- 套餐分页查询

- 删除套餐

- 修改套餐

- 起售停售套餐

## 要求：



1. 根据产品原型进行需求分析，分析出业务规则

2. 设计接口

3. 梳理表之间的关系（分类表、菜品表、套餐表、口味表、套餐菜品关系表）

4. 根据接口设计进行代码实现

5. 分别通过swagger接口文档和前后端联调进行功能测试

qury是requestparam

?看看aop切面；

?如何选择 vo /dto

中间肯定是vo/dto

![image\.png](图片和附件/image%201.png)

![image\.png](图片和附件/image%202.png)

![image\.png](图片和附件/image%205.png)

![image\.png](图片和附件/image%204.png)

if\(setmeal\.getStatus\(\)==StatusConstant\.*DISABLE*\)\{
    //*todo why*
*    *throw new Exception\(MessageConstant\.*SETMEAL\_ON\_SALE*\);
\}

这里是如何抛出异常的\-》

![image\.png](图片和附件/image%203.png)

命名：setmeal中getbyidwithdishes

@AutoFill\(OperationType\.*UPDATE*\)@Transactional@enable\.\.\.

```Java
package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@EnableCaching//开发缓存注解功能
@EnableScheduling //开启任务调度

public class SkyApplication {
    public static void main(String[] args) {
        SpringApplication.*run*(SkyApplication.class, args);
        *log*.info("server started");
    }
}
```

？返回数据必须是有data这个是什么意思查询回回想\-》getbyid中实现嘛；

？就是修改这个功能中的data是必须得是如何实现的；and如果是null自动忽视呢；



？就是为什么又header

就是stoporstart\-》mapper层代码的复用；update  就是再service层builder一个对象；

1. Builder

2. null/size》0

@Override
    public void stoporstart\(Integer status,Long id\)\{

        Setmeal setmeal=setmealMapper\.getbyid\(id\);
        List\<Dish\> dishes= dishMapper\.getBySetmealId\(id\);
        dishes\.forEach\(
                dish\-\>\{
                    if\(dish\.getStatus\(\)==StatusConstant\.*DISABLE*\)\{
                        throw new DeletionNotAllowedException\(MessageConstant\.*SETMEAL\_ON\_SALE*\);
                    \}
                \}
        \);
        //1or0如何写
        setmeal\.setStatus\(StatusConstant\.*DISABLE*\);
    \}
\}

数组参数接受前端要加上@requestparam

```Java
//可以少点sql插入类对象；
  <insert id="insertBatch" parameterType="list">
        insert into setmeal_dish
        (setmeal_id,dish_id,name,price,copies)
        values
        <foreach collection="setmealDishes" item="sd" separator=",">
            (#{sd.setmealId},#{sd.dishId},#{sd.name},#{sd.price},#{sd.copies})
        </foreach>
    </insert>
```

?其实这里要实现多少个接口or每个接口是如何进行协调的and 如何判断是不是已经实现了

![image\.png](图片和附件/image.png)

```Java
*/***
* * 根据id查询员工*
* **
* * @param id*
* * @return*
* */*
//这里是不是不太·规范就是应该是用一个比较好的employeevo比较好
public Employee getById(Long id) {
    //e
    Employee employee = employeeMapper.getById(id);
    employee.setPassword("****");
    return employee;
}
```

## 新增套餐

### 1\.1 需求分析和设计

产品原型：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018135930842\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018135930842.png)

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018140833345\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018140833345.png)

业务规则：

- 套餐名称唯一

- 套餐必须属于某个分类

- 套餐必须包含菜品

- 名称、分类、价格、图片为必填项

- 添加菜品窗口需要根据分类类型来展示菜品

- 新增的套餐默认为停售状态

接口设计（共涉及到4个接口）：

- 根据类型查询分类（已完成）

- 根据分类id查询菜品

- 图片上传（已完成）

- 新增套餐

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018141521068\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018141521068.png)

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018141606787\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018141606787.png)

数据库设计：

setmeal表为套餐表，用于存储套餐的信息。具体表结构如下：

setmeal\_dish表为套餐菜品关系表，用于存储套餐和菜品的关联关系。具体表结构如下：

### 1\.2 代码实现

#### 1\.2\.1 DishController

```Java
*/**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
***/*@GetMapping("/list")@ApiOperation("根据分类id查询菜品")public Result<List<Dish>> list(Long categoryId){
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
}
```

#### 1\.2\.2 DishService

```Java
*/**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
***/*
List<Dish> list(Long categoryId);
```

#### 1\.2\.3 DishServiceImpl

```Java
*/**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
***/*public List<Dish> list(Long categoryId) {
    Dish dish = Dish.builder()
        .categoryId(categoryId)
        .status(StatusConstant.ENABLE)
        .build();
    return dishMapper.list(dish);
}
```

#### 1\.2\.4 DishMapper

```Java
*/**
     * 动态条件查询菜品
     * @param dish
     * @return
***/*
List<Dish> list(Dish dish);
```

#### 1\.2\.5 DishMapper\.xml

```XML
<select id="list" resultType="Dish" parameterType="Dish">
    select * from dish
    <where><if test="name != null">
            and name like concat('%',#{name},'%')
        </if><if test="categoryId != null">
            and category_id = #{categoryId}
        </if><if test="status != null">
            and status = #{status}
        </if></where>
    order by create_time desc
</select>
```

#### 1\.2\.6 SetmealController

```Java
*/**
 * 套餐管理
** */*@RestController@RequestMapping("/admin/setmeal")@Api(tags = "套餐相关接口")@Slf4jpublic class SetmealController {

    @Autowiredprivate SetmealService setmealService;

    */**
     * 新增套餐
     * @param setmealDTO
     * @return
**     */*@PostMapping@ApiOperation("新增套餐")public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }
}
```

#### 1\.2\.7 SetmealService

```Java
public interface SetmealService {

    */**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
**     */*void saveWithDish(SetmealDTO setmealDTO);
}
```

#### 1\.2\.8 SetmealServiceImpl

```Java
*/**
 * 套餐业务实现
** */*@Service@Slf4jpublic class SetmealServiceImpl implements SetmealService {

    @Autowiredprivate SetmealMapper setmealMapper;
    @Autowiredprivate SetmealDishMapper setmealDishMapper;
    @Autowiredprivate DishMapper dishMapper;

    */**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
**     */*@Transactionalpublic void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        *//向套餐表插入数据*
        setmealMapper.insert(setmeal);

        *//获取生成的套餐id*Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        *//保存套餐和菜品的关联关系*
        setmealDishMapper.insertBatch(setmealDishes);
    }
}
```

#### 1\.2\.9 SetmealMapper

```Java
*/**
     * 新增套餐
     * @param setmeal
***/*@AutoFill(OperationType.INSERT)void insert(Setmeal setmeal);
```

#### 1\.2\.10 SetmealMapper\.xml

```XML
<insert id="insert" parameterType="Setmeal" useGeneratedKeys="true" keyProperty="id">
    insert into setmeal
    (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user)
    values (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime},
    #{createUser}, #{updateUser})
</insert>
```

#### 1\.2\.11 SetmealDishMapper

```Java
*/**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
***/*void insertBatch(List<SetmealDish> setmealDishes);
```

#### 1\.2\.12 SetmealDishMapper\.xml

```XML
<insert id="insertBatch" parameterType="list">
    insert into setmeal_dish
    (setmeal_id,dish_id,name,price,copies)
    values
    <foreach collection="setmealDishes" item="sd" separator=",">
        (#{sd.setmealId},#{sd.dishId},#{sd.name},#{sd.price},#{sd.copies})
    </foreach></insert>
```

### 1\.3 功能测试

略

## 套餐分页查询

### 2\.1 需求分析和设计

产品原型：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018152429246\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018152429246.png)

业务规则：

- 根据页码进行分页展示

- 每页展示10条数据

- 可以根据需要，按照套餐名称、分类、售卖状态进行查询

接口设计：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018152731141\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018152731141.png)

### 2\.2 代码实现

#### 2\.2\.1 SetmealController

```Java
*/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
***/*@GetMapping("/page")@ApiOperation("分页查询")public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
    PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
    return Result.success(pageResult);
}
```

#### 2\.2\.2 SetmealService

```Java
*/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
***/*
PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
```

#### 2\.2\.3 SetmealServiceImpl

```Java
*/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
***/*public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
    int pageNum = setmealPageQueryDTO.getPage();
    int pageSize = setmealPageQueryDTO.getPageSize();

    PageHelper.startPage(pageNum, pageSize);
    Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
    return new PageResult(page.getTotal(), page.getResult());
}
```

#### 2\.2\.4 SetmealMapper

```Java
*/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
***/*
Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
```

#### 2\.2\.5 SetmealMapper\.xml

```XML
<select id="pageQuery" resultType="com.sky.vo.SetmealVO">
    select
            s.*,c.name categoryName
    from
            setmeal s
    left join
            category c
    on
            s.category_id = c.id
    <where><if test="name != null">
            and s.name like concat('%',#{name},'%')
        </if><if test="status != null">
            and s.status = #{status}
        </if><if test="categoryId != null">
            and s.category_id = #{categoryId}
        </if></where>
    order by s.create_time desc
</select>
```

### 2\.3 功能测试

略

## 删除套餐

### 3\.1 需求分析和设计

产品原型：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018153756531\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018153756531.png)

业务规则：

- 可以一次删除一个套餐，也可以批量删除套餐

- 起售中的套餐不能删除

接口设计：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018154541067\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018154541067.png)

### 3\.2 代码实现

#### 3\.2\.1 SetmealController

```Java
*/**
     * 批量删除套餐
     * @param ids
     * @return
***/*@DeleteMapping@ApiOperation("批量删除套餐")public Result delete(@RequestParam List<Long> ids){
    setmealService.deleteBatch(ids);
    return Result.success();
}
```

#### 3\.2\.2 SetmealService

```Java
*/**
     * 批量删除套餐
     * @param ids
***/*void deleteBatch(List<Long> ids);
```

#### 3\.2\.3 SetmealServiceImpl

```Java
*/**
     * 批量删除套餐
     * @param ids
***/*@Transactionalpublic void deleteBatch(List<Long> ids) {
    ids.forEach(id -> {
        Setmeal setmeal = setmealMapper.getById(id);
        if(StatusConstant.ENABLE == setmeal.getStatus()){
            *//起售中的套餐不能删除*throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
    });

    ids.forEach(setmealId -> {
        *//删除套餐表中的数据*
        setmealMapper.deleteById(setmealId);
        *//删除套餐菜品关系表中的数据*
        setmealDishMapper.deleteBySetmealId(setmealId);
    });
}
```

#### 3\.2\.4 SetmealMapper

```Java
*/**
     * 根据id查询套餐
     * @param id
     * @return
***/*@Select("select * from setmeal where id = #{id}")
Setmeal getById(Long id);

*/**
     * 根据id删除套餐
     * @param setmealId
***/*@Delete("delete from setmeal where id = #{id}")void deleteById(Long setmealId);
```

#### 3\.2\.5 SetmealDishMapper

```Java
*/**
     * 根据套餐id删除套餐和菜品的关联关系
     * @param setmealId
***/*@Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")void deleteBySetmealId(Long setmealId);
```

### 3\.3 功能测试

略

## 修改套餐

### 4\.1 需求分析和设计

产品原型：

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018160214225\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018160214225.png)

接口设计（共涉及到5个接口）：

- 根据id查询套餐

- 根据类型查询分类（已完成）

- 根据分类id查询菜品（已完成）

- 图片上传（已完成）

- 修改套餐

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018160915177\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018160915177.png)

[https://file\+\.vscode\-resource\.vscode\-cdn\.net/c%3A/LearningResources/no\-washing\-club/sky\_take\_out/resources/resources/day04/assets/image\-20221018160949864\.png](https://file+.vscode-resource.vscode-cdn.net/c%3A/LearningResources/no-washing-club/sky_take_out/resources/resources/day04/assets/image-20221018160949864.png)

### 4\.2 代码实现

#### 4\.2\.1 SetmealController

```Java
*/**
     * 根据id查询套餐，用于修改页面回显数据
     *
     * @param id
     * @return
***/*@GetMapping("/{id}")@ApiOperation("根据id查询套餐")public Result<SetmealVO> getById(@PathVariable Long id) {
    SetmealVO setmealVO = setmealService.getByIdWithDish(id);
    return Result.success(setmealVO);
}

*/**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
***/*@PutMapping@ApiOperation("修改套餐")public Result update(@RequestBody SetmealDTO setmealDTO) {
    setmealService.update(setmealDTO);
    return Result.success();
}
```

#### 4\.2\.2 SetmealService

```Java
*/**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
***/*
SetmealVO getByIdWithDish(Long id);

*/**
     * 修改套餐
     * @param setmealDTO
***/*void update(SetmealDTO setmealDTO);
```

#### 4\.2\.3 SetmealServiceImpl

```Java
*/**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
***/*public SetmealVO getByIdWithDish(Long id) {
    Setmeal setmeal = setmealMapper.getById(id);
    List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

    SetmealVO setmealVO = new SetmealVO();
    BeanUtils.copyProperties(setmeal, setmealVO);
    setmealVO.setSetmealDishes(setmealDishes);
    
    return setmealVO;
}

*/**
     * 修改套餐
     *
     * @param setmealDTO
***/*@Transactionalpublic void update(SetmealDTO setmealDTO) {
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal);

    *//1、修改套餐表，执行update*
    setmealMapper.update(setmeal);

    *//套餐id*Long setmealId = setmealDTO.getId();

    *//2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete*
    setmealDishMapper.deleteBySetmealId(setmealId);

    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
    setmealDishes.forEach(setmealDish -> {
        setmealDish.setSetmealId(setmealId);
    });
    *//3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert*
    setmealDishMapper.insertBatch(setmealDishes);
}
```

#### 4\.2\.4 SetmealDishMapper

```Java
*/**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param setmealId
     * @return
**     */*@Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
```

### 4\.3 功能测试

略

## 起售停售套餐

### 5\.1 需求分析和设计

产品原型：

业务规则：

- 可以对状态为起售的套餐进行停售操作，可以对状态为停售的套餐进行起售操作

- 起售的套餐可以展示在用户端，停售的套餐不能展示在用户端

- 起售套餐时，如果套餐内包含停售的菜品，则不能起售

接口设计：

### 5\.2 代码实现

#### 5\.2\.1 SetmealController

```Java
*/**
     * 套餐起售停售
     * @param status
     * @param id
     * @return
***/*@PostMapping("/status/{status}")@ApiOperation("套餐起售停售")public Result startOrStop(@PathVariable Integer status, Long id) {
    setmealService.startOrStop(status, id);
    return Result.success();
}
```

#### 5\.2\.2 SetmealService

```Java
*/**
     * 套餐起售、停售
     * @param status
     * @param id
***/*void startOrStop(Integer status, Long id);
```

#### 5\.2\.3 SetmealServiceImpl

```Java
*/**
     * 套餐起售、停售
     * @param status
     * @param id
***/*public void startOrStop(Integer status, Long id) {
    *//起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"*if(status == StatusConstant.ENABLE){
        *//select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?*
        List<Dish> dishList = dishMapper.getBySetmealId(id);
        if(dishList != null && dishList.size() > 0){
            dishList.forEach(dish -> {
                if(StatusConstant.DISABLE == dish.getStatus()){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
    }

    Setmeal setmeal = Setmeal.builder()
        .id(id)
        .status(status)
        .build();
    setmealMapper.update(setmeal);
}
```

#### 5\.2\.4 DishMapper

```Java
*/**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
***/*@Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
List<Dish> getBySetmealId(Long setmealId);
```

### 5\.3 功能测试

略

