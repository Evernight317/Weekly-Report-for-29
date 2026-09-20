# Stream流

`Stream`（Stream流），JDK8新增API( `java.util.stream.` )，可以用于操作集合或者数组的数据；
优势：Stream流大量使用Lambda，能够更加方便简洁地操作集合、数组中的数据；

比如，收集张姓的三字名字：

Stream支持 **链式编程** ；

同时，下方代码意思是将过滤得到的名字放入一个List容器中；filter意为过滤；

```Java
	List<String> names  = new ArrayList<>();
	List<String> lists1 = new ArrayList<>();
	Collections.addAll(names,"张无忌","周芷若","赵敏","张强");
	for (String name : names) {
    	if(name.startsWith("张") && name.length() == 3) {
        	lists1.add(name);
    	}
	}
	// List<String> lists = names.stream().filter(s -> s.startsWith("张")
	//  && s.length() == 3).collect(Collectors.toList());
	List<String> lists2 = names.stream().filter(s -> s.startsWith("张"))
	.filter(s -> s.length()==3).collect(Collectors.toList());
	System.out.println(lists1);
	System.out.println(lists2);
```

1. 获取Stream流，与数据源建立联系；
2. 调用中间方法对数据处理，且中间方法支持链式编程；
3. 获取结果并将其收集到集合中返回；

## 获取Stream流

Stream是一个接口

### 获取集合Stream流

| Collection提供方法             |  |
| ------------------------------ | - |
| `default Stream<E> stream()` |  |

对于Map， **无法直接获取Stream流** ；由于stream方法只对Collection提供，因此需要先获取key或者value的Collection，才能获得对应的键/值集合的Stream流，或者获得Entry对象的集合，再获得Entry集合的Stream流；

### 获取数组Stream流

| Arrays类提供方法                                  | Stream类提供方法                              |
| ------------------------------------------------- | --------------------------------------------- |
| `public static <T> Stream<T> stream(T[] array)` | `public static<T> Stream<T> of(T...values)` |

```Java
Stream<String> stream = lists.stream();
Stream<String> stream = sets.stream();
stream.filter(s -> s.contains("德")).forEach(s -> System.out.println (s));

String[] names = {};
Stream s1 = Arrays.stream(names);
Stream s2 = Stream.of(names);
```

## 中间方法

中间方法：调用完成后会返回新的Stream流，可以继续使用（支持链式编程)；

|                                                                |                                                        |
| -------------------------------------------------------------- | ------------------------------------------------------ |
| `Stream<T> filter(Predicate<? super T> predicate)`           | 对流中的数据进行过滤                                   |
| `Stream<I> sorted()`                                         | 对元素进行升序排序                                     |
| `Stream<I> sorted(Comparator<? super I> comparator)`         | 自定义排序                                             |
| `Stream<T> limit(long maxSize)`                              | 获取前几个元素                                         |
| `Stream<T> skip(long n)`                                     | 跳过前几个元素                                         |
| `Stream<T> distinct()`                                       | 去除流中重复的元素                                     |
| `<R> Stream<R> map(Function<? super L,？ extends R> mapper)` | 对元素进行映射/加工，并返回对应的新流                  |
| `static<T> Stream<T> concat(Stream a, Stream b)`             | 合并a和b两个流为一个流                                 |
| `Stream<Object> concat(Stream s1,Stream s2)`                 | 将两个流合并成一个流并返回；当两个流类型不同则为Object |

`map`方法：如找出身高>168 的学生，并且名字去重后输出；

```Java
// map(s -> s.getName())
// 相当于把学生对象转换成学生名字放入Stream流中
// 此时流中留下的则是身高>168的学生名字，而不是学生对象
// 再用distinct去重后即可输出
// 当然可以用特定类型方法引用简化为
// map(Student::getName)
students.stream().filter(s -> s.getHeight()> 168)
  	.map(s -> s.getName())
	.distinct().forEach(System.out::println);
```

`distinct`方法：判断是否相同同样是hashCode和equals，因此内容一样的去重要重写方法；

## 终结方法

不会返回Stream流，无法继续调用；

|                                                           |                                            |
| --------------------------------------------------------- | ------------------------------------------ |
| `void forEach(Consumer action)`                         | 对流中元素执行遍历                         |
| `long count()`                                          | 统计流的元素个数                           |
| `Optional<I> max(Comparator<? super I> comparator)`     | 获取流的最大值元素<br />同样可以自定义比较 |
| `Optional<I></i> min(Comparator<? super I> comparator)` | 获取流的最小值元素                         |

max后会存到`Optional`容器里，获取需要 `max(...).get()`

### 终结方法-收集Stream流

把Stream流操作后的结果转回到集合或者数组中去返回；

开发时最终还是用集合数组；

流只能 **收集一次** ；类似于迭代器？

|                                    |                |
| ---------------------------------- | -------------- |
| `R collect(Collector collector)` | 收集到指定集合 |
| `Object[] toArray()`             | 收集到指定数组 |

`Collectors.toList()`

`toSet`

`toMap`注意，要声明key和value的具体内容，而且要注意预处理 **去重** ，collect方法无法自动去重；

```Java
.collect(Collectors.toMap
		(a -> a.getName(), a -> a.getHeight());
```

收集成数组

默认为`Object`，如果要指定类型需在形参中声明；
