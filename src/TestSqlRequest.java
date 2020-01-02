import java.sql.*;

public class TestSqlRequest {

    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "kerzrehm";
    static final String PASSWD = "kerzrehm";

    public static void main(String args[]) throws SQLException {
    	
    	SqlRequest base = new SqlRequest(USER, PASSWD);
    	base.getFirstIdOpen("ARTISTE", "IDARTISTE");
    	base.closeConnection();
        
    }


}
