import java.io.IOException
import java.util._
import org.apache.hadoop.fs.Path
import org.apache.hadoop.conf._
import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce._
import org.apache.hadoop.util._
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
//import BIDMat.MatFunctions._
//import BIDMat.IMat
import scala.reflect.Manifest
import java.io.PrintWriter
import org.apache.commons.cli.Options
 
 
 
object WordCount2 extends Configured with Tool {

  //input key, input value. output key, output value
  
  val Regex = "(\\d+)".r;

  class Map extends Mapper[LongWritable, Text, Text, IntWritable] {
    var one: IntWritable = new IntWritable(1);
    //var word: Text = new Text();
  	
    //context is a member of mapper class
    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context) {
      // var a : IMat = 1\2\3
      var line: String = value.toString();
      
      Regex.findAllIn(line).matchData foreach {
        m => context.write(new Text(m.group(1)), one)
      }
      
      //context.write(new Text(line), one);
      
      /**for(word <- line.split(" ")){
        context.write(new Text(word), one)
      }**/
 
      /*
      var tokenizer: StringTokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
        word.set(tokenizer.nextToken());
        context.write(word, one);
      }
      */
    }
  }
 
  class Reduce extends Reducer[Text, IntWritable, Text, IntWritable] {
    // input values has to be iterable in java land not scala land
    override def reduce(key: Text, values: java.lang.Iterable[IntWritable], context:Reducer[Text, IntWritable, Text, IntWritable]#Context) {
      val ivalues = values.iterator
      val svals = new scala.collection.Iterator[Int]{
        def hasNext = ivalues.hasNext
	def next = ivalues.next.get
      }
 
      //val sum = (svals : scala.collection.Iterator[Int]).reduceLeft (
      //  (a: Int, b: Int) => a + b
      //)
      //context.write(key, new IntWritable(sum))
      context.write(key, new IntWritable(1))
    /*
      var sum: Int = 0;
      while (values.hasNext()) {
        sum += values.next().get();
      }
      context.write(key,new IntWritable(sum))
      */
    }
  }
  
  def run(args: Array[String]) =
  {
		  var conf = super.getConf()
//		  Configuration.dumpConfiguration(conf,new PrintWriter(System.out)) // for verfying your conf file 
//	      println("Libjars: " + conf.get("tmpjars")); //for making sure your jars have been include
	      var job : Job = new Job(conf,"WordCount2")
	      job
              job setJarByClass(this.getClass())
	      job setOutputKeyClass classOf[Text]
	      job setOutputValueClass classOf[IntWritable]
 
	      job setMapperClass classOf[Map]
	      job setCombinerClass classOf[Reduce]
	      job setReducerClass classOf[Reduce]
  	      
  	      FileInputFormat.addInputPath(job, new Path(args(1)))
  	      FileOutputFormat.setOutputPath(job, new Path(args(2)))
  	      job waitForCompletion(true) match {
		    case true => 0
		    case false => 1
		  }
   }
  
  def main(args: Array[String]) {
    var  c : Configuration = new Configuration()
    var res : Int = ToolRunner.run(c,this, args)
    System.exit(res);
  }
  
}
