import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ServletProcessor1 {

	public void process(Request request, Response response) {

		String uri = request.getUri();
		int index=-1;
		String servletName = uri.substring(uri.lastIndexOf("/") + 1);
		if(servletName.contains("?")){
			index=servletName.indexOf("?");
			servletName=servletName.substring(0,index);
		}
		
		
		//--------^_^---����XML�ļ�
		File file =new File(System.getProperty("user.dir")+"/src/web.xml");
		String xml="";
		try {
			FileReader fd= new FileReader(file);
			int len=(int)file.length();
			char[] charArr=new char[len];
			fd.read(charArr);
			xml=String.valueOf(charArr);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(servletName);
		Document doc=Jsoup.parse(xml);
		Elements servlets=doc.getElementsByTag("servlet-mapping");
		for (Element element : servlets) {
			//-----
			if(servletName.equals(element.getElementsByTag("url-pattern").get(0).text().substring(1))){
				String servletBigName=element.getElementsByTag("servlet-name").get(0).text();
				URLClassLoader loader = null;
				try {
					URLStreamHandler streamHandler = null;
					// �����������
					loader = new URLClassLoader(new URL[] { new URL(null, "file:"
							+ Constants.WEB_SERVLET_ROOT, streamHandler) });
				} catch (IOException e) {
					System.out.println(e.toString());
				}
				Class<?> myClass = null;
				try {
					// ���ض�Ӧ��servlet��
					myClass = loader.loadClass(servletBigName);
				} catch (ClassNotFoundException e) {
					System.out.println(e.toString());
				}
				Servlet servlet = null;

				try {
					// ����servletʵ��
					servlet = (Servlet) myClass.newInstance();
					// ִ��ervlet��service����
					servlet.service((ServletRequest) request,
							(ServletResponse) response);
				} catch (Exception e) {
					System.out.println(e.toString());
				} catch (Throwable e) {
					System.out.println(e.toString());
				}
			}
		}
		
		// ������������ڴ�ָ��JAR�ļ���Ŀ¼������
		

		

	}
}
