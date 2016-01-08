package set;

import java.io.Reader;
import java.util.List;

import junit.enums.DataType;
import junit.enums.DriverType;
import junit.rule.DataSource;
import junit.rule.DriverService;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Rule;
import org.junit.Test;

import entity.entity.t_ajd_agent;
import entity.inter.t_ajd_agent_inter;
import requestentity.*;
import responseentity.ResponseArray;
import send.sendHttp;

public class test {
	@Rule
	public DriverService ds = new DriverService();
	
	
	@Test
	@DataSource(Drive=DriverType.excel,minmax=DataType.min)
	public void test1(){
		new sendHttp().send(new ReqEntity_login());
		System.out.println(ResponseArray.getInstance("test1", "login").getValue("resq.data"));
		new sendHttp().send(new ReqEntity_redpackets());
		System.out.println(ResponseArray.getInstance("test1", "redpackets").getValue("resp.data"));
//		new sendHttp().send(new ReqEntity_esf_detail());
//		System.out.println(ResponseArray.getInstance("test1", "esf_detail").msg);
	}
	//@Test
	public void test2(){
		SqlSessionFactory sqlSessionFactory = null; 
		try {
			Reader reader = Resources.getResourceAsReader("entity/config/MyBatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SqlSession session = sqlSessionFactory.openSession();
		//entity.Test test = (entity.Test)session.selectOne("Test.selectByID",15);
		//System.out.println(test.getsStub());
		List<t_ajd_agent> ajd = session.getMapper(t_ajd_agent_inter.class).selectBysMobile("15183308585");
		for(t_ajd_agent t:ajd){
			System.out.println(t.getiCreateTime());
		}
		System.out.println();
	}
}
