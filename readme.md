## Java Promise ##
There are many IO's operation without dependency.If we make them asynchronous,The IO's throughput would be imporved greatly.As we know,asynchronous programming will make the code hard to read.Promise is the specification to resolve it.However, Java doesn't provided it to us.This project,**Java promise** is promise implements by Java1.7 exatcly. 

The Promise object is used for deferred and asynchronous computations. A Promise represents an operation that hasn't completed yet, but is expected in the future.

### Syntax ###
```java
    new Promise(new Resolver() {
        public void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception {
            ...
        }
    } )
```
Parameters

1. Resolver is the same as:
	`function(resolve, reject){...}`
	
### Description ###
A Promise is in one of these states:

- *pending*:initial state, not fulfilled or rejected.
- *fulfilled*:meaning that the operation completed successfully.
- *rejected*:meaning that the operation failed.
 ![](https://mdn.mozillademos.org/files/8633/promises.png)

### Method ###
#### static method ####
1. Promise.all(Promise... promises)
	`Promise.all(Promise.resolve(1), Promise.resolve(2))`
2. Promise.race(Promise... promises)
	`Promise.race(Promise.resolve(1), Promise.reject(2))`
3. Promise.resolve(value)
	`Promise.resolve("resovle")`
4. Promise.reject(value)
	`Promise.reject("reject")`

#### instance method ####
1. promise.then(OnFulfill onFulfill, OnReject onReject)
2. promise.then(OnFulfill onFulfill)
2. promise.Catch(OnReject onReject)

Example1
```java	
	//if the random index is equal to 0 , print "success result : 1",
	//the random index is equal to 1, print "error result : 0"
     new Promise(new Resolver() {
            public void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception {
                int index = new Random().nextInt(2);
                if (index == 0) onFulfill.execute(index);
                else onReject.execute(index);
            }
        }).then(new OnFulfill<String, Integer>() {
            public String execute(Integer i) {
                return "success result : " + i;
            }
        }, new OnReject() {
            public Object execute(Object args) {
                return "error result : " + args;
            }
        })
```
Example2
```java
	//print "last result : 1"
	Promise.resolve("1").then(new OnFulfill() {
            public Object execute(Object args) {
                System.out.println("last result : " + args);
                return null;
            }
        });
	
	//print "This is my exception, last result is 1"
	Promise.resolve("1").then(new OnFulfill() {
            public Object execute(Object args) throws Exception{
                throw new Exception("This is my exception, last result is "+args);
            }
        }).Catch(new OnReject<Object, Exception>() {
            public Object execute(Exception e) throws Exception {
                System.out.println(e.getMessage());
                return null;
            }
        });
```

About Promise :

[https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise "Promise - JavaScript | MDN")

[https://www.promisejs.org/](https://www.promisejs.org/ "Promise")
