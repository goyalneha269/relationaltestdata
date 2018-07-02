# relationaltestdata
The utilities available online are limited to the number of rows. So we designed a utility to generate large amount of test data.This utility comes with the feature of generating relational database. XML file is given as an input to the Class test.java and the output is a csv file.

**XML File
Create a xml file defining the schema of each table of your database. Following are the possible XML tags.

        <filename>: Under this tag enter the name of output CSV file along with .csv extension.
        
        <rows>: No. of rows of CSV files you want to generate.
        
        <column>: This tag is used to define the schema of individual columns of the table. Under <column> tag one can add                          following subtags.
    
            <columnname>: Enter the columnname
    
            <datatype>: Enter the datatype of the column. The allowed datatypes are int, float, char, datetime
    
            <length>: Enter the limit of datalength. 
                    Example: If the length of int datatype is 5 then the utility will generate rows with values ranging from 1                       to 99999. Similarly for char if the length is 5 then the utility will generate random strings of length 5.
    
            <startrange>,<endrange>: If you want the column values to be in certain range then enter the start and end range                                values under the <startrange> and <endrange> tag respectively. Example: <startrange>1</startrange>                             <endrange>100</endrange> then the column will have values ranging from 1 to 100.
    
            <menu>: This is "," separated list of values to choose from.
            <unique>: This tag is used to declare certain tag as primary key. If you want to make a column as primary key of                          that table then add this tag to that column with value yes (<unique>yes</unique>), also define the                              <startrange> and <endrange> tags. This will generate primary key values starting from startrange ending                          with endrange and incremented by one. Make sure that the value under <endrange> and <rows> tag are same                          otherwise it will not work as primary key.
    
            If the given column is referring the primary key of any other table then define the <filepath> and                               <primarykeyname>tags.       
             <filepath>: Enter the complete XML file path of the parent table.
             <primarykeyname>: Enter the column name of the primary key of parent table.

      <foreignkey>: If this table refers the primary of some other table (parent table) then this tag is used. You can have mulitple foreign keys referring multiple parent tables in a single child table. Under this tag the following subtags are defined.
            <foreignkeyname>: Enter the name of the column which is going to refer the primary key of parent table.

Enclose all the above tags within <records> tag.

**Repository
Artifact can be fetched from bintray. Add following <repository> element to your <repositories> section in pom.xml:
  
    <repository>
        <id>bintray-smartcat-labs-maven</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/smartcat-labs/maven</url>
    </repository>

**Dependency
Add the <dependency> element to your <dependencies> section in pom.xml with specific version.ranger you need:

    <dependency>
        <groupId>io.smartcat</groupId>
        <artifactId>ranger</artifactId>
        <version>${version.ranger}</version>
    </dependency>

Similarly, dependency can be added to any other build tool supporting maven dependencies.

Code
Copy the code Test.java and TableCreater.java and run Test.java class. Enter the number of tables you want to create and input the complete XML file paths of those tables.

Sample XML files has been added in the repository.
**This project uses the dependency of https://github.com/smartcat-labs/ranger.
