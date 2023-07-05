# Java 序列化工具性能对比

## 实验目的：

在FastJSON、JDK、ProtoBuffer、ProtoStuff、Kyro 几种常见的JSON和二进制序列化框架中找到一个能够在序列化和反序列化时使用时间较少，以及序列化后字节数较少的工具。

## 实验环境：

### 本机环境：

```plain
OS: Windows 10 专业版 x64
JVM: JDK 11.0.18, Java HotSpot(TM) 64-Bit Server VM, 11.0.18+9-LTS-195
CPU: AMD Ryzen 7 5825U with Radeon Graphics 2.00 GHz
Cores: 8
RAM: 32 GB
```
### 工具包版本：

| 工具名       | 版本     |
|:-----------|:-------|
| JDK        | 1.8    |
| protobuf   | 3.23.0 |
| kryo       | 5.5.0  |
| protostuff | 1.7.4  |
| fastjson2  | 2.0.34 |

## 实验特色：

1. 考虑生产环境的多样情况，采取冷启动和热启动两种采样方式，其中冷启动获取的数据即每次测试程序第一次启动的数据。热启动即多次循环得到的数据。
2. 实验数据分为较简单数据和复杂数据两种，其中复杂数据采用实际生产环境的数据进行测试。
3. 实验结果以当前主要使用的Protobuf作为基准，计算其他工具与其对应指标差值的百分比，便于直观感受差异。
4. 因为实操发现在相同配置下性能测试结果不够稳定，每次的序列化和反序列化时间偏差在10%左右，并且各个工具的偏差程度各不相同，甚至引起性能排序变化。所以采用内外两层循环，内层循环控制一次**运行**的序列化和反序列化次数，外层循环控制**运行**次数，每次运行会启动多个线程对每种工具进行序列化测试。这样做的好处在于实测能使性能测试的结果偏差更小。
5. 使用JMH的预热功能改善多次测量指标有偏差的情况。
## 实验结果：

性能测试由于实验结果不稳定，截图采用多次同一配置下实验的中位值，或者采用多张截图。

1. 实验一：
    1. 实验配置：
        1. 简单对象序列化和反序列化循环1000次；
        2. 复杂对象序列化和反序列化循环200次；
        3. 外层循环200次（控制上述两个循环的次数，最终取平均值）；
    2. 实验截图：
<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/200200 1.png?raw=true" alt=""/>
</p>

<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/200x200 2.png?raw=true" alt=""/>
</p>

2. 实验二：
    1. 实验配置：
        1. 简单对象序列化和反序列化循环1000次；
        2. 复杂对象序列化和反序列化循环200次；
        3. 外层循环50次（控制上述两个循环的次数，最终取平均值）；
    2. 实验截图：


3. 实验三：
    1. 实验配置：
        1. 简单对象序列化和反序列化循环1000次；
        2. 复杂对象序列化和反序列化循环50次；
        3. 外层循环200次（控制上述两个循环的次数，最终取平均值）；
    2. 实验截图：


4. 实验四：
    1. 实验配置：
        1. 简单对象序列化和反序列化循环1000次；
        2. 复杂对象序列化和反序列化循环50次；
        3. 外层循环50次（用于控制上述两个循环的次数，最终取平均值）；
    2. 实验截图：


5. 结论（针对复杂对象）：
    1. 由上面几个实验可以看出来，**随着循环次数增多，各个工具的平均的序列化和反序列化的绝对时间都降低**，从minSerialization 的时间也可以看出，说明热启动和冷启动的区分是很有必要的。
    2. 在热启动的情况下，Kryo和ProtoStuff的序列化耗时比protobuf多20%以上，并且随着循环次数增多，**序列化耗时与Protobuf的差异率上升**，在内外循环各200次的情况下，序列化差异率超过100%。整体看来，这两者**反序列化的差异率是负数，说明耗时较protobuf短**，但是随着循环次数反序列化的差异率也在上升。在内外循环各50的情况下，Kryo和ProtoStuff在序列化和反序列化总时长上占优势，但是内外循环上升到200的时候protobuf更占优。
    3. **Kryo在序列化后的字节大小方面更占优势**，相比于使用FareSearchResponse契约的Protobuf，Kryo对于其对应的POJO的序列化字节数减少17.75%，是比较大的提升。
    4. ProtoStuff和Kryo冷启动时由于需要Schema的抽取，所以都比较慢，差距和Protobuf这种预编码的二进制序列化工具较大，ProtoStuff最好会多24%的整体序列化和反序列化的时间消耗，Kryo会多100%以上的时间消耗。
## 改进方向：

1. 使用JMH的预热功能改善多次测量指标有偏差的情况。
2. 使用图形化界面展示更直观。
3. 鉴于二进制的序列化工具对于当前生产环境的数据支持更好，后期可以添加更多二进制的序列化工具进行序列化反序列化，如：Avro、Thrift等。

## 更新：
1. 使用JMH的预热功能改善多次测量指标有偏差的情况。
2. 使用图形化界面展示更直观。
3. 采用更大的12MB数据Master

## Master (~ 12MB) file template
    [
      '{{repeat(50)}}',
      {
        id: '{{index()}}',
        balance: '{{floating(1000, 4000, 2, "$0,0.00")}}',
        age: '{{integer(20, 40)}}',
        sword: '{{random("blue", "brown", "green")}}',
        name: '{{firstName()}} {{surname()}}',
        gender: '{{gender()}}',
        company: '{{company().toUpperCase()}}',
        email: '{{email()}}',
        phone: '+3 {{phone()}}',
        address: '{{integer(100, 999)}} {{street()}}, {{city()}}, {{state()}}, {{integer(100, 10000)}}',
        about: '{{lorem(1, "paragraphs")}}',
        started: '{{date(new Date(2014, 0, 1), new Date(), "YYYY-MM-ddThh:mm:ss Z")}}',
        latitude: '{{floating(-90.000001, 90)}}',
        longitude: '{{floating(-180.000001, 180)}}',
        tasks: [
          '{{repeat(100, 200)}}',
          {
            id: '{{index()}}',
            description: '{{lorem(5, "words")}}'
          }
        ],
        forces: [
          '{{repeat(25, 100)}}',
          {
            id: '{{index()}}',
            title: '{{lorem(10, "words")}}',
            started: '{{date(new Date(2014, 0, 1), new Date(), "YYYY-MM-ddThh:mm:ss Z")}}',
            areas: [
              '{{repeat(10, 20)}}',
              '{{lorem(1, "words")}}'
            ],
            description: '{{lorem(10, "paragraphs")}}'
          }
        ]
      }
    ]

### Master serialization

<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/SerMaster.png?raw=true" alt=""/>
</p>

### Master deserialization

<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/DesrMaster.png?raw=true" alt=""/>
</p>


## Complex (~ 141KB) file templates not provided

### Complex serialization

<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/SerComplex.png?raw=true" alt=""/>
</p>

### Complex deserialization

<p>
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/DesrComplex.png?raw=true" alt=""/>
</p>

### 实验结论
- 与更新前实验结论一致

## 参考
- 代码仓库：
  - https://github.com/oyy2000/tech1-benchmarks/tree/master/jmh-serialization
  - https://github.com/oyy2000/serialization-test
- 测试工具改编自：
  - [keyhunter/serialization-test: Java serialization test. FastJSON Jackson XML JDK ProtoBuffer Kyro几种序列化方式的demo和简单的性能测试及对比。 (github.com)](https://github.com/keyhunter/serialization-test)
  - [tech1-io/tech1-benchmarks: Java JMH Benchmarks repository. No Longer Supported. (github.com)](https://github.com/tech1-io/tech1-benchmarks)
- 参考资料： 
  - [ShouZhiDuan/dsz-serialized: JAVA应用中常见序列化工具数据处理性能测试 (github.com)](https://github.com/ShouZhiDuan/dsz-serialized)

