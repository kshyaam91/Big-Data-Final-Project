#! /bin/bash
# A script to run the Hadoop wordcount example program on all
# files stored in the /home/hduser/tmp/snip directory (*.txt)


oldTstamp=0
oldTstamp1=0

while true; do
newTstamp=`stat -c %Y /home/deekshith/tmp/input1.txt`
newTstamp1=`stat -c %Y /home/deekshith/tmp/input2.txt`
# Compare the Timestamps

if [ $newTstamp -gt $oldTstamp ]
then
	echo "New data to be processed \n"
	oldTstamp=$newTstamp

	# If new data is available start the hadoop processes
	cd /home/deekshith/Downloads/hadoop-1.2.1

	# Clear all the snip directories under tmp
	bin/hadoop fs -rmr /user/deekshith/tmp/snip-hdfs
	bin/hadoop fs -rmr /user/deekshith/tmp/snip-output

	# Copy files into the HDFS
	echo "Copying the new data into HDFS \n"
	bin/hadoop dfs -copyFromLocal /home/deekshith/tmp/newtimestamp.txt /user/deekshith/tmp/snip-hdfs
	bin/hadoop dfs -ls /user/deekshith/tmp/snip-hdfs

	# Run the mapreduce job
	echo "Running the map-reduce"
	echo
	bin/hadoop jar /home/deekshith/tmp/myjar.jar saMain /user/deekshith/tmp/snip-hdfs /user/deekshith/tmp/snip-output
	echo
	bin/hadoop dfs -cat /user/deekshith/tmp/snip-output/part-00000 > /home/deekshith/tmp/hadoop-output.txt
	chmod 777 /home/deekshith/tmp/hadoop-output.txt
	echo "Output processed to /home/deekshith/tmp/hadoop-output"
fi
#done

if [ $newTstamp1 -gt $oldTstamp1 ]
then
	echo "New commit to be processed \n"
	oldTstamp1=$newTstamp1

	# If new commit is pressed start the update power processes
	cd /home/deekshith/Desktop

	# Clear all the snip directories under tmp
	# bin/hadoop fs -rmr /user/hduser/tmp/snip-hdfs
	# bin/hadoop fs -rmr /user/hduser/tmp/snip-output

	# Copy files into the HDFS
	# echo "Copying the new data into HDFS \n"
        #bin/hadoop dfs -copyFromLocal /home/hduser/tmp/input1.txt /user/hduser/tmp/snip-hdfs
	#bin/hadoop dfs -ls /user/hduser/tmp/snip-hdfs

	# Run the mapreduce job
	echo "updating power"
	echo
	java -cp /home/deekshith/Desktop/myPowerjar.jar Gen_Array_After_Commit
	#echo
	#bin/hadoop dfs -cat /user/hduser/tmp/snip-output/part-00000 > /home/hduser/tmp/hadoop-output.txt
	chmod 777 /home/deekshith/Desktop/Randatabase.txt
	echo "power updated view at /home/deekshith/Desktop/Randatabase.txt "
fi
done

exit
