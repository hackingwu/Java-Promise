CompletableFuture实现了CompletionStage接口，CompletionStage提供了很多方法，有
thenApply, thenAccept, thenRun这三个常用的返回，表示当一个future完成后继续执行的动作。
这个三个方法后面都可以加上后缀Async，即thenApplyAsync, thenAccpetAsync, thenRunAsync 。
他们与没有Async的区别是，xxxAsync多接受了一个参数Executor，表示继续执行的动作是在哪个Executor上执行。
如果没有指定Executor，是在asyncPool上执行,即和stream().parallel()用的是同一个Exceutor，
其线程数受CPU核心数限制。如果你要执行的动作是IO密集型的，可以自定义Executor提供更多的线程。
`
CompletableFuture.java 
private static final Executor asyncPool = useCommonPool ?
         ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();
         `
         
那么thenApply, thenAccept, thenRun 三个方法有啥区别。还是看其方法签名(入参和返回值）。
thenApply public <U> CompletionStage<U> thenApply(Function<? super T,? extends U> fn);
接受一个Function，可以拿到上一个future的返回值，并且处理完，返回一个对象，可以继续被处理。
thenAccept public CompletionStage<Void> thenAccept(Consumer<? super T> action);
接受一个Consumer，可以拿到上一个future的返回值，但是处理完没有返回值。
thenApply public CompletionStage<Void> thenRun(Runnable action);
接受一个Runable，拿不到上一个返回值，处理完没有返回值。