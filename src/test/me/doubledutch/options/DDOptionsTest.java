package me.doubledutch.options;

//    Copyright [2015] [DoubleDutch]
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.is;

public class DDOptionsTest{
	// Test parser
	@Test
	public void parseIdentifierOption() {
		String[] result=DDOptions.splitOption("foo");
		assertThat(result[0],is("foo"));
		assertThat(result[1],is("boolean"));
		assertThat(result[2],is("foo"));
	}

	@Test
	public void parseIdentifierTypeOption() {
		String[] result=DDOptions.splitOption("foo:integer");
		assertThat(result[0],is("foo"));
		assertThat(result[1],is("integer"));
		assertThat(result[2],is("foo"));
	}

	@Test
	public void parseIdentifierTypeMappingOption() {
		String[] result=DDOptions.splitOption("foo:integer->bar");
		assertThat(result[0],is("foo"));
		assertThat(result[1],is("integer"));
		assertThat(result[2],is("bar"));
	}

	// Test get option by name
	@Test
	public void getOption() {
		String result=DDOptions.getOption("foo",new String[]{"baz","foo:integer->bar"});
		assertThat(result,is("foo:integer->bar"));
		result=DDOptions.getOption("baz",new String[]{"baz","foo:integer->bar"});
		assertThat(result,is("baz"));
		result=DDOptions.getOption("bar",new String[]{"baz","foo:integer->bar"});
		assertThat(result,nullValue());
	}

	// Test needs argument lofic
	@Test
	public void needsArgumentLogic() {
		assertThat(DDOptions.needsArgument("baz"),is(false));
		assertThat(DDOptions.needsArgument("baz:boolean"),is(false));
		assertThat(DDOptions.needsArgument("baz:string"),is(true));
		assertThat(DDOptions.needsArgument("baz:integer->foo"),is(true));
	}

	// Test full functionality
	@Test
	public void setMatchingField(){
		OptionsTarget t=new OptionsTarget();
		assertThat(t.foo,is(false));
		DDOptions.setOptions(t,new String[]{"-foo"},"foo:boolean");
		assertThat(t.foo,is(true));
	}

	@Test
	public void setPublicIntegerField(){
		OptionsTarget t=new OptionsTarget();
		DDOptions.setOptions(t,new String[]{"-foo","42"},"foo:integer->public_i");
		assertThat(t.public_i,is(42));
	}

	@Test
	public void setPrivateIntegerField(){
		OptionsTarget t=new OptionsTarget();
		DDOptions.setOptions(t,new String[]{"-foo","42"},"foo:integer->private_i");
		assertThat(t.getPrivateI(),is(42));
	}

	@Test
	public void setPublicStringField(){
		OptionsTarget t=new OptionsTarget();
		DDOptions.setOptions(t,new String[]{"-m","Hello World!"},"m:string->public_s");
		assertThat(t.public_s,is("Hello World!"));
	}

	@Test
	public void setPublicStringMethod(){
		OptionsTarget t=new OptionsTarget();
		DDOptions.setOptions(t,new String[]{"-m","Hello World!"},"m:string->setPublicS");
		assertThat(t.public_s,is("Hello World!"));
	}
}