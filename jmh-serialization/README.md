# jmh-serialization

JMH Java Serialization (JDK, FastJson, Jackson, FST, Kryo, MessagePack) application for different complexity JSONs structure

# Master ~ 12MB file
### Master JSON template

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

<p align="center">
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/SerMaster.png?raw=true" alt=""/>
</p>

### Master deserialization

<p align="center">
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/DesrMaster.png?raw=true" alt=""/>
</p>




# Complex ~ 141KB file

### Complex serialization

<p align="center">
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/SerComplex.png?raw=true" alt=""/>
</p>

### Complex deserialization

<p align="center">
	<img src="https://github.com/oyy2000/tech1-benchmarks/blob/master/jmh-serialization/img/DesrComplex.png?raw=true" alt=""/>
</p>

