package coreDrivers;

import java.io.IOException;

import org.testng.annotations.Test;

import utils.ReportingUtils;

/**
 * 
 * Test driver Method names can be any name The name of the TestCase should be
 * same as ExcelSheet and the same as the TestCase in Quality Centre The name of
 * the TestCase should not contain spaces " " The name of the TestCase should
 * not contain period "."
 * 
 * @author Pankaj Sharma
 *
 */
public class TestDriver_DB {

	private static String testCaseNameQC;
	private static String dataTableName;
	private static String testId;
	// private static String testPhase;

	ComponentDriver report = new ComponentDriver();

	public TestDriver_DB() throws IOException {
		ComponentDriver.cleanResultDirectory();
		ReportingUtils.setup();
	}

	@Test(groups = { "Before" })
	public static void EI2_User_Integ_Ul_Obj_Before() throws Exception {

		dataTableName = "EI2_User_Integ_Ul_Obj_Before";
		testCaseNameQC = "EI2_User_Integ_Ul_Obj_Before";
		testId = "53";
		
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);

	}

	@Test(groups = { "Before" })
	public static void EI2_User_Integ_Ul_Obj_After() throws Exception {

		dataTableName = "EI2_User_Integ_Ul_Obj_After";
		testCaseNameQC = "EI2_User_Integ_Ul_Obj_After";
		testId = "54";

		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Cloudera_Integration_Before() throws Exception {

		dataTableName = "Cloudera_Integration_Before";
		testCaseNameQC = "Cloudera_Integration_Before";
		testId = "32";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Cloudera_Integration_After() throws Exception {

		dataTableName = "Cloudera_Integration_After";
		testCaseNameQC = "Cloudera_Integration_After";
		testId = "33";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);

	}

	@Test(groups = { "Before", "ChangeRunDemo" })
	public static void SCHEMA_NAME_on_TABLES_Before() throws Exception {
		
		dataTableName = "SCHEMA_NAME_on_TABLES_Before";
		testCaseNameQC = "SCHEMA_NAME_on_TABLES_Before";
		testId = "34";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void SCHEMA_NAME_on_TABLES_After() throws Exception {
		
		dataTableName = "SCHEMA_NAME_on_TABLES_After";
		testCaseNameQC = "SCHEMA_NAME_on_TABLES_After";
		testId = "36";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	} // //

	@Test(groups = { "Before" })
	public static void PRIV_on_GRANTED_PRIV_Before() throws Exception {

		dataTableName = "PRIV_on_GRANTED_PRIV_Before";
		testCaseNameQC = "PRIV_on_GRANTED_PRIV_Before";
		testId = "37";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		} //

	@Test(groups = { "After" })
	public static void PRIV_on_GRANTED_PRIV_After() throws Exception {

		dataTableName = "PRIV_on_GRANTED_PRIV_After";
		testCaseNameQC = "PRIV_on_GRANTED_PRIV_After";
		testId = "38";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void ROLE_NAME_on_GRNTD_ROLES_Before() throws Exception {

		dataTableName = "ROLE_NAME_on_GRNTD_ROLES_Before";
		testCaseNameQC = "ROLE_NAME_on_GRNTD_ROLES_Before";
		testId = "39";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void ROLE_NAME_on_GRNTD_ROLES_After() throws Exception {

		dataTableName = "ROLE_NAME_on_GRNTD_ROLES_After";
		testCaseNameQC = "ROLE_NAME_on_GRNTD_ROLES_After";
		testId = "40";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Cnt_OBJECT_TYPE_on_OBJS_Before() throws Exception {

		dataTableName = "Cnt_OBJECT_TYPE_on_OBJS_Before";
		testCaseNameQC = "Cnt_OBJECT_TYPE_on_OBJS_Before";
		testId = "41";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Cnt_OBJECT_TYPE_on_OBJS_After() throws Exception {

		dataTableName = "Cnt_OBJECT_TYPE_on_OBJS_After";
		testCaseNameQC = "Cnt_OBJECT_TYPE_on_OBJS_After";
		testId = "42";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void SCHEMA_NAME_on_OBJECTS_Before() throws Exception {

		dataTableName = "SCHEMA_NAME_on_OBJECTS_Before";
		testCaseNameQC = "SCHEMA_NAME_on_OBJECTS_Before";
		testId = "43";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void SCHEMA_NAME_on_OBJECTS_After() throws Exception {

		dataTableName = "SCHEMA_NAME_on_OBJECTS_After";
		testCaseNameQC = "SCHEMA_NAME_on_OBJECTS_After";
		testId = "44";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Cnt_SchNmeObjType_on_OBJ_Before() throws Exception {

		dataTableName = "Cnt_SchNmeObjType_on_OBJ_Before";
		testCaseNameQC = "Cnt_SchNmeObjType_on_OBJ_Before";
		testId = "45";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Cnt_SchNmeObjType_on_OBJ_After() throws Exception {

		dataTableName = "Cnt_SchNmeObjType_on_OBJ_After";
		testCaseNameQC = "Cnt_SchNmeObjType_on_OBJ_After";
		testId = "46";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Teradata_Integration_Before() throws Exception {

		dataTableName = "Teradata_Integration_Before";
		testCaseNameQC = "Teradata_Integration_Before";
		testId = "47";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Teradata_Integration_After() throws Exception {

		dataTableName = "Teradata_Integration_After";
		testCaseNameQC = "Teradata_Integration_After";
		testId = "48";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void ARW_UC_Integrate_Ul_Obj_Before() throws Exception {

		dataTableName = "ARW_UC_Integrate_Ul_Obj_Before";
		testCaseNameQC = "ARW_UC_Integrate_Ul_Obj_Before";
		testId = "49";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After", "ChangeRunDemo" })
	public static void ARW_UC_Integrate_Ul_Obj_After() throws Exception {

		dataTableName = "ARW_UC_Integrate_Ul_Obj_After";
		testCaseNameQC = "ARW_UC_Integrate_Ul_Obj_After";
		testId = "50";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void ARW_DP_layer_Before() throws Exception {

		dataTableName = "ARW_DP_layer_Before";
		testCaseNameQC = "ARW_DP_layer_Before";
		testId = "51";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void ARW_DP_layer_After() throws Exception {

		dataTableName = "ARW_DP_layer_After";
		testCaseNameQC = "ARW_DP_layer_After";
		testId = "52";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void EI2_DP_Layer_Before() throws Exception {

		dataTableName = "EI2_DP_Layer_Before";
		testCaseNameQC = "EI2_DP_Layer_Before";
		testId = "55";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void EI2_DP_Layer_After() throws Exception {

		dataTableName = "EI2_DP_Layer_After";
		testCaseNameQC = "EI2_DP_Layer_After";
		testId = "56";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Glacier_DP_Tera_Integ_Before() throws Exception {

		dataTableName = "Glacier_DP_Tera_Integ_Before";
		testCaseNameQC = "Glacier_DP_Tera_Integ_Before";
		testId = "57";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Glacier_DP_Tera_Integ_After() throws Exception {

		dataTableName = "Glacier_DP_Tera_Integ_After";
		testCaseNameQC = "Glacier_DP_Tera_Integ_After";
		testId = "58";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Glacier_BT_Tera_Integ_Before() throws Exception {

		dataTableName = "Glacier_BT_Tera_Integ_Before";
		testCaseNameQC = "Glacier_BT_Tera_Integ_Before";
		testId = "59";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Glacier_BT_Tera_Integ_After() throws Exception {

		dataTableName = "Glacier_BT_Tera_Integ_After";
		testCaseNameQC = "Glacier_BT_Tera_Integ_After";
		testId = "60";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Glacier_DP_Integ_Db_Before() throws Exception {

		dataTableName = "Glacier_DP_Integ_Db_Before";
		testCaseNameQC = "Glacier_DP_Integ_Db_Before";
		testId = "61";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After", "ChangeRunDemo" })
	public static void Glacier_DP_Integ_Db_After() throws Exception {

		dataTableName = "Glacier_DP_Integ_Db_After";
		testCaseNameQC = "Glacier_DP_Integ_Db_After";
		testId = "62";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void Glacier_User_Inte_ul_Obj_Before() throws Exception {

		dataTableName = "Glacier_User_Inte_ul_Obj_Before";
		testCaseNameQC = "Glacier_User_Inte_ul_Obj_Before";
		testId = "63";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void Glacier_User_Inte_ul_Obj_After() throws Exception {

		dataTableName = "Glacier_User_Inte_ul_Obj_After";
		testCaseNameQC = "Glacier_User_Inte_ul_Obj_After";
		testId = "64";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void SI_DP_Tera_Integ_Before() throws Exception {

		dataTableName = "SI_DP_Tera_Integ_Before";
		testCaseNameQC = "SI_DP_Tera_Integ_Before";
		testId = "65";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After", "ChangeRunDemo" })
	public static void SI_DP_Tera_Integ_After() throws Exception {

		dataTableName = "SI_DP_Tera_Integ_After";
		testCaseNameQC = "SI_DP_Tera_Integ_After";
		testId = "66";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void SI_BT_Tera_Integ_Before() throws Exception {

		dataTableName = "SI_BT_Tera_Integ_Before";
		testCaseNameQC = "SI_BT_Tera_Integ_Before";
		testId = "67";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void SI_BT_Tera_Integ_After() throws Exception {

		dataTableName = "SI_BT_Tera_Integ_After";
		testCaseNameQC = "SI_BT_Tera_Integ_After";
		testId = "68";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void SI_UC_Integ_Db_After() throws Exception {

		dataTableName = "SI_UC_Integ_Db_After";
		testCaseNameQC = "SI_UC_Integ_Db_After";
		testId = "69";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void SI_UC_Integ_Db_Before() throws Exception {

		dataTableName = "SI_UC_Integ_Db_Before";
		testCaseNameQC = "SI_UC_Integ_Db_Before";
		testId = "70";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "Before" })
	public static void SI_User_Integ_ul_Obj_Before() throws Exception {

		dataTableName = "SI_User_Integ_ul_Obj_Before";
		testCaseNameQC = "SI_User_Integ_ul_Obj_Before";
		testId = "71";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

	@Test(groups = { "After" })
	public static void SI_User_Integ_ul_Obj_After() throws Exception {

		dataTableName = "SI_User_Integ_ul_Obj_After";
		testCaseNameQC = "SI_User_Integ_ul_Obj_After";
		testId = "72";
		ComponentDriver.driver(dataTableName, testCaseNameQC, testId);
		
	}

//   @AfterSuite
//    public void OpenReport() {
//          if(SettingsUtils.getTagNameValue("DisplayReport").equalsIgnoreCase("ON")) {
//                 DisplayReport();  
//          }
//          
//   }

}