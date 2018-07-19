package DataFactory.artifact;

import static io.smartcat.ranger.BuilderMethods.*;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.smartcat.ranger.ObjectGenerator;
import io.smartcat.ranger.ObjectGeneratorBuilder;

public class TableCreater extends Thread{

	private String filepath;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private FileWriter fileWriter = null;
	private boolean fk = false;
	private String foreignKeyDetails[];
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public void path(String filepath) {
		this.filepath = filepath;
		
		/*
		 * SparkConf conf=new SparkConf().setAppName("Test Data");
		 * conf.set("spark.driver.allowMultipleContexts", "true");
		 * JavaSparkContext jsc=new JavaSparkContext(conf);
		 */

	}

	public void run() {

		System.out.println("starting "+this.getName());
		try {
			boolean range = false;
			int length = 0;

			File inputFile = new File(filepath);
			ArrayList<String> headers = new ArrayList();
			ObjectGeneratorBuilder ogb = new ObjectGeneratorBuilder();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			if (doc.getElementsByTagName("foreignkey").item(0) != null) {
				fk=true;
				NodeList foreignList = doc.getElementsByTagName("foreignkey");

				foreignKeyDetails=new String[foreignList.getLength()];
				
				for (int m = 0; m < foreignList.getLength(); m++) {
					Node foreignNode = foreignList.item(m);

					if (foreignNode.getNodeType() == Node.ELEMENT_NODE) {
						Element foreignElement = (Element) foreignNode;
						foreignKeyDetails[m] = foreignElement.getElementsByTagName("foreignkeyname").item(0)
								.getTextContent();
					}
				}
			}

			NodeList nList = doc.getElementsByTagName("column");

			long rows = Long.parseLong(doc.getElementsByTagName("rows").item(0).getTextContent());
			String filename = doc.getElementsByTagName("filename").item(0).getTextContent();

			fileWriter = new FileWriter(filename);

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				String[] arrMenu = null;

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (fk && contains(eElement.getElementsByTagName("columnname").item(0).getTextContent()) != -1) {
						String[] fkrange=getfkrange(eElement.getElementsByTagName("filepath").item(0).getTextContent(),eElement.getElementsByTagName("primarykeyname").item(0).getTextContent());
						ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(), random(range(Long.parseLong(fkrange[0]),Long.parseLong(fkrange[1]))));
						headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
					} else {
						String menuList = null;
						if (eElement.getElementsByTagName("menu").item(0) != null)
							menuList = eElement.getElementsByTagName("menu").item(0).getTextContent();
						if (menuList != null && !menuList.isEmpty()) {
							range = true;
							arrMenu = menuList.split(",");
						}

						if (range == true) {
							ogb=ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(),
									random(arrMenu));
							headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
							range = false;
						} else {
							if (eElement.getElementsByTagName("unique").item(0) != null) {
								ogb=ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(),
										circular(range(Long.parseLong(eElement.getElementsByTagName("startrange").item(0).getTextContent()),Long.parseLong(eElement.getElementsByTagName("endrange").item(0).getTextContent())), 1L));
								headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
							} else {

								switch (eElement.getElementsByTagName("datatype").item(0).getTextContent()) {
								case "int":
									if (eElement.getElementsByTagName("startrange").item(0) != null
											&& eElement.getElementsByTagName("endrange").item(0) != null) {
										int start = Integer.parseInt(
												eElement.getElementsByTagName("startrange").item(0).getTextContent());
										int end = Integer.parseInt(
												eElement.getElementsByTagName("endrange").item(0).getTextContent());
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(start, end)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									} else {
										length = Integer.parseInt(
												eElement.getElementsByTagName("length").item(0).getTextContent());
										int value = 9;
										for (int i = 2; i <= length; i++) {
											value = value * 10 + 9;
										}
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(0, value)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									break;

								case "float":
									if (eElement.getElementsByTagName("startrange").item(0) != null
											&& eElement.getElementsByTagName("endrange").item(0) != null) {
										float start = Float.valueOf(
												eElement.getElementsByTagName("startrange").item(0).getTextContent());
										float end = Float.valueOf(
												eElement.getElementsByTagName("endrange").item(0).getTextContent());
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(start, end)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									else
									{
									length = Integer
											.parseInt(eElement.getElementsByTagName("length").item(0).getTextContent());
									float valuef = 9.0f;
									for (int i = 2; i < length; i++) {
										valuef = valuef * 10 + 9;
									}
									ogb = ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(),
											random(range(0.0f, valuef)));
									headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
										break;
								
								case "long":
									if (eElement.getElementsByTagName("startrange").item(0) != null
											&& eElement.getElementsByTagName("endrange").item(0) != null) {
										long start = Long.parseLong(
												eElement.getElementsByTagName("startrange").item(0).getTextContent());
										long end = Long.parseLong(
												eElement.getElementsByTagName("endrange").item(0).getTextContent());
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(start, end)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									} else {
										length = Integer.parseInt(
												eElement.getElementsByTagName("length").item(0).getTextContent());
										long value = 9;
										for (int i = 2; i <= length; i++) {
											value = value * 10 + 9;
										}
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(0L, value)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									break;

								case "double":
									if (eElement.getElementsByTagName("startrange").item(0) != null
											&& eElement.getElementsByTagName("endrange").item(0) != null) {
										double start = Double.parseDouble(
												eElement.getElementsByTagName("startrange").item(0).getTextContent());
										double end = Double.parseDouble(
												eElement.getElementsByTagName("endrange").item(0).getTextContent());
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(start, end)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									else
									{
									length = Integer.parseInt(eElement.getElementsByTagName("length").item(0).getTextContent());
									double valuef = 9.0d;
									for (int i = 2; i < length; i++) {
										valuef = valuef * 10 + 9;
									}
									ogb = ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(),
											random(range(0.0d, valuef)));
									headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									break;

								case "char":
									int lengthValue = Integer
											.parseInt(eElement.getElementsByTagName("length").item(0).getTextContent());
									ogb = ogb.prop(eElement.getElementsByTagName("columnname").item(0).getTextContent(),
											randomContentString(constant(lengthValue)));
									headers.add(eElement.getElementsByTagName("columnname").item(0).getTextContent());
									break;

								case "datetime":
									if (eElement.getElementsByTagName("startrange").item(0) != null
											&& eElement.getElementsByTagName("endrange").item(0) != null) {
										String start = eElement.getElementsByTagName("startrange").item(0)
												.getTextContent();
										String end = eElement.getElementsByTagName("endrange").item(0).getTextContent();
										Date startdate = DATE_FORMAT.parse(start);
										Date enddate = DATE_FORMAT.parse(end);
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(startdate, enddate)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									} else {

										Date startdate = DATE_FORMAT.parse("2018-01-01");
										Date enddate = DATE_FORMAT.parse("2018-03-31");
										ogb = ogb.prop(
												eElement.getElementsByTagName("columnname").item(0).getTextContent(),
												random(range(startdate, enddate)));
										headers.add(
												eElement.getElementsByTagName("columnname").item(0).getTextContent());
									}
									break;

								}
							}
						}
					}
				}
			}

			ObjectGenerator<Map<String, Object>> user = ogb.build();
			int numberofcolumns = headers.size();
			int temp1 = 0;
			for (temp1 = 0; temp1 < numberofcolumns - 1; temp1++) {
				fileWriter.append(headers.get(temp1));
				fileWriter.append(COMMA_DELIMITER);
			}
			fileWriter.append(headers.get(temp1));
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (long i = 0; i < rows; i++) {
				int temp = 0;
				Map<String,Object> row=user.next();
				for (temp = 0; temp < numberofcolumns - 1; temp++) {
					if(row.get(headers.get(temp)) instanceof Date)
					{
						fileWriter.append(DATE_FORMAT.format((Date)row.get(headers.get(temp))));
						fileWriter.append(COMMA_DELIMITER);
					}
					else
					{
						fileWriter.append(row.get(headers.get(temp)).toString());
						fileWriter.append(COMMA_DELIMITER);
					}
				}
				fileWriter.append(row.get(headers.get(temp)).toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			fileWriter.flush();
			fileWriter.close();

			System.out.println("CSV file created successfully "+this.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private String[] getfkrange(String filepath, String primarykeyname) {
		// TODO Auto-generated method stub
		File inputFile = new File(filepath);
		String[] fkrange=new String[2];
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			if (doc.getElementsByTagName("primarykey").item(0) != null) {
				Node primaryList[] = doc.getElementsByTagName("primarykey");
				
				for(int i=0;i<primaryList.length;i++)
				{
					Element primaryElement=(Element)primaryList.item(i);
				
					if(primaryElement.getElementsByTagName("primarykeyname").item(0).getTextContent().equalsIgnoreCase(primarykeyname))
					{
						fkrange[0]=primaryElement.getElementsByTagName("startrange").item(0).getTextContent();
						fkrange[1]=primaryElement.getElementsByTagName("endrange").item(0).getTextContent();
						return fkrange;
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private int contains(String columnname) {
		// TODO Auto-generated method stub

		for (int i = 0; i < foreignKeyDetails.length; i++) {
			if (foreignKeyDetails[i].equalsIgnoreCase(columnname))
				return i;
		}
		return -1;
	}
}
