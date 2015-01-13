DDOptions
==========

DDOptions is a quick and dirty options parsing solution for Java. How quick?
and how dirty? ...Let's look at a piece of sample code!

````
public class Hack{
	boolean flag=false;

	public static void main(String[] args){
		Hack obj=new Hack();
		DDOptions.setOptions(obj,args,"flag");
	}
}
````

If you run Hack with a -flag option, the variable flag will now be set to true.

````
java Hack -flag
````

You don't get the full handling of parameter validation and documentation as
you would with a full fledged options library like
[JCommander](http://jcommander.org/), but in just one line of code you let your
prototype accept command line configuration with no additional work.

## Configuration

You can call setOptions with any number of options configured. Each option is
configured through a single string composed of three parts: The name of the
option followed by :, the type of the option followed by -> and finally, the
name of the target.

````
<configuration> = <name> ':' ( <type> ( '->' <target> )? )?
````

If you omit the target, the name of the option will be assumed to match the
name of the target field or method. If you omit the target and the type, it
will be assumed to be a boolean matching the name of the option.

Let's look at a sample from an internal DoubleDutch app that uses DDOptions:

````
DDOptions.setOptions(obj,args,
	"threads:integer->setThreadCount",
	"log:string->setLogFilename",
	"verbose");
````

As you can probably guess from the naming conventions in this sample, DDOptions
is able to both set fields directly on objects as well as call setter methods.

### Mapping

The purpose of DDOptions is to provide a quick and easy way to accept command
line options in your Java based prototypes and hacks - thus, it will attempt to
find any suitable mapping for your options.

A simple option like "verbose" from the last sample will be attempted mapped to
a field named verbose, a method named verbose or a method named setVerbose.

It does not matter what access modifiers are used in the object you are trying
to set the options on - DDOptions will bypass private and protected access
modifiers to set the value anyways.

### Types

So far, DDOptions support the following option types:

 * boolean - this is the default option type and is expected to not have any
   arguments when given on the command line.
 * string - any string value will be accepted, how spaces and qouted characters
   are handled will be up to your shell environment.
 * integer - these will be parsed using Javas Integer.parseInt method and then
   assigned as an int value.
 * float - these will be parsed using Javas Float.parseFloat method and then
   assigned as a float value.
 * timestamp - current time stamps are required to be a full ISO 8601 timestamp
   string as accepted by javax.xml.bind.DatatypeConverter.parseDateTime. It
   will be assigned as a long value representing a standard milliseconds time
   stamp.

## Error Handling

DDOptions will return a list of errors in the form of strings. These will
include errors related to the configuration strings you passed it, as well as
options missing arguments or options with malformed arguments.

It will however not throw any errors based on unused or unexpected options in
the input. This is by design so that it is possible to call setOptions multiple
times with different targets to configure your solution as shown in the
following sample:

````
DDOptions.setOptions(threadPool,args,"threads:integer->setThreadCount");
DDOptions.setOptions(logger,args,"log:string->setFilename");
````
