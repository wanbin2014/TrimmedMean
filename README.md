#TrimmedMean#

<p>
TrimmedMean can compute mean value after trim some peak value (include top n and bottom n value). TrimmedMean is realtime, you can retrieve mean value in anytime. Because it comput exception value in a fixed size slide windows,  It's not accurate totally, but it's is enough in practice.
</p>
<p>
TrimmedMean is thread safe. you can call setValue method in different thread concurrently. 
</p>

## For Example ##

```
TrimmedMean mean = new TrimmedMean(10,2,2,false);
for(int i = 0; i < 100; i++) {
      mean.setValue((long)i);
}
System.out.println(mean.getValue());
System.out.println(mean.getTopExceptionValue());
System.out.println(mean.getBottomExceptionValue());
for(int i = 0; i < 100; i++) {
      mean.setValue((long)i);
}
System.out.println(mean.getValue());
```
