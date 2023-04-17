module iStockMasterPro {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;
	requires java.desktop;
	requires junit;
	
	opens application to javafx.graphics, javafx.fxml;
	opens sqlData to javafx.base;
	opens pages to javafx.fxml;
	opens controllers to javafx.fxml;
}
