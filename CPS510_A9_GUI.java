import java.awt.GridBagConstraints;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * This program demonstrates how to make database connection with Oracle
 
 *
 */
public class CPS510_A9_GUI {
	ArrayList<JTable> tables = new ArrayList<JTable>();
	public static Connection conn1 = null;
	public static JTable currentTable;
	public static JScrollPane jsp = new JScrollPane();
	public static TableRowSorter<TableModel> rowSorter;
 
    public static void main(String[] args) {
    	// GUI stuff
    	JFrame frame = new JFrame("CPS510 A9");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,750);
        
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(new GridBagLayout());
        
        JLabel SearchLb = new JLabel("Search:");
        
        JTextField TableNameTF = new JTextField();
        JTextField EntryTF = new JTextField();
        JTextField UpdateTF = new JTextField();
        
        TableNameTF.setPreferredSize(new Dimension(150,30));
        EntryTF.setPreferredSize(new Dimension(150,30));
        UpdateTF.setPreferredSize(new Dimension(150,30));
        
        JButton DTbutton = new JButton("Drop Table");
        JButton CTbutton = new JButton("Create Table");
        JButton STbutton = new JButton("Show Table");
        JButton PTbutton = new JButton("Populate Table");
        JButton DEbutton = new JButton("Delete Entry");
        JButton SEbutton = new JButton("Search Entry");
        JButton UEbutton = new JButton("Update Entry");
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(DTbutton,c);
        
        c.gridx = 1;
        c.gridy = 0;
        panel.add(CTbutton,c);
       
        c.gridx = 2;
        c.gridy = 0;
        panel.add(STbutton,c);
        
        c.gridx = 3;
        c.gridy = 0;
        panel.add(TableNameTF,c);
        
        c.gridx = 0;
        c.gridy = 1;
        panel.add(DEbutton,c);
        
        c.gridx = 1;
        c.gridy = 1;
        panel.add(PTbutton,c);
        
        c.gridx = 2;
        c.gridy = 1;
        panel.add(SearchLb,c);
        
        c.gridx = 3;
        c.gridy = 1;
        panel.add(EntryTF,c);
        
        c.gridx = 2;
        c.gridy = 2;
        panel.add(UEbutton,c);
        
        c.gridx = 3;
        c.gridy = 2;
        panel.add(UpdateTF,c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.insets = new Insets(50, 0, 0, 0);
        currentTable = new JTable();
        rowSorter = new TableRowSorter<>(currentTable.getModel());
        currentTable.setRowSorter(rowSorter);
        
        jsp = new JScrollPane(currentTable);
        panel.add(jsp,c);
        
        frame.add(panel);
        
        EntryTF.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = EntryTF.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = EntryTF.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        

        try {
    	
            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");
 
           
            //   String dbURL1 = "jdbc:oracle:thin:username/password@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs
																						
         	
             String dbURL1 = "jdbc:oracle:thin:system/password@localhost:1521:xe";
			/* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation. 
			xe is the sid, change according to what you setup during installation. */
			
			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }
            
            
            // Populate table button
            PTbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String tableName = TableNameTF.getText();
                    System.out.println("table name: "+TableNameTF.getText());
                    try {
                        try (Connection connect = DriverManager.getConnection(dbURL1);
                            Statement statement = connect.createStatement()) {

                            // execute the statement here
                        	String sql = "INSERT INTO "+tableName+" VALUES (1,2)";
                        	
                        	statement.executeUpdate(sql);
                            System.out.println("Inserted values into given table..."); 
                        	

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    } finally {
                        try {
                            if (conn1 != null && !conn1.isClosed()) {
                            	conn1.close();
                            }
                 
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            // Drop table button
            DTbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String tableName = TableNameTF.getText();
                    System.out.println("table name: "+TableNameTF.getText());
                    try {
                        try (Connection connect = DriverManager.getConnection(dbURL1);
                            Statement statement = connect.createStatement()) {

                            // execute the statement here
                        	String sql = "DROP TABLE "+tableName;
                        	
                        	statement.executeUpdate(sql);
                            System.out.println("Dropped table in given database..."); 
                        	

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    } finally {
                        try {
                            if (conn1 != null && !conn1.isClosed()) {
                            	conn1.close();
                            }
                 
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            // Create Table button
            CTbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String tableName = TableNameTF.getText();
                    System.out.println("table name: "+TableNameTF.getText());
                    try {
                        try (Connection connect = DriverManager.getConnection(dbURL1);
                            Statement statement = connect.createStatement()) {

                            // execute the statement here
                        	String sql = "CREATE TABLE "+tableName+
                                    " (COL1 INTEGER, " +
                                    " COL2 INTEGER)";   
                        	
                        	statement.executeUpdate(sql);
                            System.out.println("Created table in given database..."); 
                        	

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    } finally {
                        try {
                            if (conn1 != null && !conn1.isClosed()) {
                            	conn1.close();
                            }
                 
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            // modify row button
            UEbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
                	
                	if (currentTable.getSelectedRow() != -1) {
                              
                        
                        try {
                            try (Connection connect = DriverManager.getConnection(dbURL1);
                                Statement statement = connect.createStatement()) {
                            	String tableName = TableNameTF.getText();
                            	String newValue = UpdateTF.getText();
                            	
                                System.out.println("table name: "+TableNameTF.getText());
                                System.out.println("new value: "+UpdateTF.getText());
                            	DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
                            	
                            	
                            	String col1Name = currentTable.getColumnName(0);
                            	String selValue = currentTable.getModel().getValueAt(currentTable.getSelectedRow(), 0).toString();
                            	
                            	System.out.println("selected value: "+selValue);
                            	
                                // execute the statement here
                            	String sql = "UPDATE "+tableName+" SET "+col1Name+" = "+newValue+"WHERE "+col1Name+" = "+selValue;
                            	
                            	statement.executeUpdate(sql);
                                System.out.println("Updated entry from table..."); 

                                //model.removeRow(currentTable.getSelectedRow());
                            	

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                        } finally {
                            try {
                                if (conn1 != null && !conn1.isClosed()) {
                                	conn1.close();
                                }
                     
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
            
            // Delete row button
            DEbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
                	
                	if (currentTable.getSelectedRow() != -1) {
                              
                        
                        try {
                            try (Connection connect = DriverManager.getConnection(dbURL1);
                                Statement statement = connect.createStatement()) {
                            	String tableName = TableNameTF.getText();
                                System.out.println("table name: "+TableNameTF.getText());
                            	DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
                            	
                            	System.out.println(currentTable.getModel().getValueAt(currentTable.getSelectedRow(), 0));

                                // execute the statement here
                            	String sql = "DELETE FROM "+tableName+" WHERE "+currentTable.getColumnName(0)+" = "+currentTable.getModel().getValueAt(currentTable.getSelectedRow(), 0);
                            	
                            	statement.executeUpdate(sql);
                                System.out.println("Deleted entry from table..."); 
                             // remove selected row from the model
                                model.removeRow(currentTable.getSelectedRow());
                            	

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                        } finally {
                            try {
                                if (conn1 != null && !conn1.isClosed()) {
                                	conn1.close();
                                }
                     
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
      
            // when Show Table button is clicked
            STbutton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                	String tableName = TableNameTF.getText();
                    System.out.println("table name: "+TableNameTF.getText());
                    try {
                        try (Connection connect = DriverManager.getConnection(dbURL1);
                            Statement statement = connect.createStatement()) {

                            // execute the statement here
                        	ResultSet rs = statement.executeQuery("select * from "+tableName);
                        	ResultSetMetaData rsmd = rs.getMetaData();
                        	int cols = rsmd.getColumnCount();
                        	Vector colNames = new Vector();
                        	Vector data = new Vector();
                        	for (int i = 1; i <= cols;i++) {
                        		colNames.addElement(rsmd.getColumnName(i));
                        	}
                        	
                        	while (rs.next()) {
                				Vector row = new Vector(cols);
                				for(int i = 1; i <= cols; i++) {
                					row.addElement(rs.getObject(i));
                				}
                				data.addElement(row);
                			}
                        	
                        	currentTable = new JTable(data, colNames) {
                        		public Class getColumnClass(int column) {
                        			for(int row = 0; row < getRowCount(); row++) {
                        				Object o = getValueAt(row, column);
                        				if (o != null) {
                        					return o.getClass();
                        				}
                        			}
                        			return Object.class;
                        		}
                        	};	// the table that is currently being displayed
                        	rowSorter = new TableRowSorter<>(currentTable.getModel());
                        	rowSorter.setRowFilter(null);
                        	currentTable.setRowSorter(rowSorter);
                        	currentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                        	
                        	c.gridx = 0;
                            c.gridy = 3;
                            c.gridwidth = 4;
                            c.insets = new Insets(50, 0, 0, 0);
                            panel.remove(jsp);
                            
                            jsp = new JScrollPane(currentTable);
                            panel.add(jsp,c);
                            
                           
                            panel.revalidate();
                            panel.repaint();

                        } catch (SQLException ex) {
                            //ex.printStackTrace();
                        	currentTable = new JTable();
                        	c.gridx = 0;
                            c.gridy = 3;
                            c.gridwidth = 4;
                            c.insets = new Insets(50, 0, 0, 0);
                            panel.remove(jsp);
                            
                            jsp = new JScrollPane(currentTable);
                            panel.add(jsp,c);
                            
                           
                            panel.revalidate();
                            panel.repaint();
                        }

                    } finally {
                        try {
                            if (conn1 != null && !conn1.isClosed()) {
                            	conn1.close();
                            }
                 
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    
                }
            });
            //--- End of Show Table button
           
            
		
            //In your database, you should have a table created already with at least 1 row of data. In this select query example, table testjdbc was already created with at least 2 rows of data with columns NAME and NUM.
			//When you enter your data into the table, please make sure to commit your insertions to ensure your table has the correct data. So the commands that you need to type in Sqldeveloper are
			// CREATE TABLE TESTJDBC (NAME varchar(8), NUM NUMBER);
            // INSERT INTO TESTJDBC VALUES ('ALIS', 67);
            // INSERT INTO TESTJDBC VALUES ('BOB', 345);
            // COMMIT;
			
			String query = "select NAME, NUM from TESTJDBC";
			int rows = 0;
			String[][] testData = new String[5][2];
							
			try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery(query);

			//If everything was entered correctly, this loop should print each row of data in your TESTJDBC table.
			// And you should see the results as follows:
			// Connected with connection #1
			// ALIS, 67
			// BOB, 345
			
			while (rs.next()) {
				String name = rs.getString("NAME");
				int num = rs.getInt("NUM");
				//System.out.println(name + ", " + num);
				testData[rows][0] = name;
				testData[rows][1] = Integer.toString(num);
				rows++;
			}
			} catch (SQLException e) {
				System.out.println("SQLException "+e.getErrorCode());
			}
			String colNames[] = {"Name", "Num"};
			JTable testTable = new JTable(testData, colNames);
			testTable.setBounds(30, 40, 200, 300);
//			c.gridx = 0;
//            c.gridy = 1;
//            c.gridwidth = 4;
//            c.insets = new Insets(50, 0, 0, 0);
//            JScrollPane sp = new JScrollPane(testTable);
//            frame.add(sp,c);
			
			//System.out.println("Rows: "+rows);

			frame.setVisible(true); // at the end
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
     
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
			

    }
}