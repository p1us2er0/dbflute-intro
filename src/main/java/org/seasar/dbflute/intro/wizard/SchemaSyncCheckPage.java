package org.seasar.dbflute.intro.wizard;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.seasar.dbflute.emecha.eclipse.plugin.wizards.client.DBFluteNewClientPageResult;

public class SchemaSyncCheckPage extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField databaseInfoUrlText;
	private JTextField databaseInfoSchemaText;
	private JTextField databaseInfoUserText;
	private JTextField databaseInfoPasswordText;

	/**
	 * Create the application.
	 */
	public SchemaSyncCheckPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {

		this.setLayout(null);

		JLabel databaseInfoUrlLabel = new JLabel("Url");
		databaseInfoUrlLabel.setBounds(10, 10, 150, 20);
		this.add(databaseInfoUrlLabel);

		databaseInfoUrlText = new JTextField();
		databaseInfoUrlText.setBounds(150, 10, 300, 20);
		databaseInfoUrlText.setColumns(10);
		this.add(databaseInfoUrlText);

		JLabel databaseInfoSchemaLabel = new JLabel("Schema");
		databaseInfoSchemaLabel.setBounds(10, 35, 150, 20);
		this.add(databaseInfoSchemaLabel);

		databaseInfoSchemaText = new JTextField();
		databaseInfoSchemaText.setBounds(150, 35, 300, 20);
		databaseInfoSchemaText.setColumns(10);
		this.add(databaseInfoSchemaText);

		JLabel databaseInfoUserLabel = new JLabel("User(*)");
		databaseInfoUserLabel.setBounds(10, 60, 150, 20);
		this.add(databaseInfoUserLabel);

		databaseInfoUserText = new JTextField();
		databaseInfoUserText.setBounds(150, 60, 300, 20);
		databaseInfoUserText.setColumns(10);
		this.add(databaseInfoUserText);

		JLabel databaseInfoPasswordLabel = new JLabel("Password");
		databaseInfoPasswordLabel.setBounds(10, 85, 150, 20);
		this.add(databaseInfoPasswordLabel);

		databaseInfoPasswordText = new JTextField();
		databaseInfoPasswordText.setBounds(150, 85, 300, 20);
		databaseInfoPasswordText.setColumns(10);
		this.add(databaseInfoPasswordText);
	}

	public DBFluteNewClientPageResult asResult() {

		final String databaseInfoUrl = databaseInfoUrlText.getText();
		final String databaseInfoSchema = databaseInfoSchemaText.getText();
		final String databaseInfoUser = databaseInfoUserText.getText();
		final String databaseInfoPassword = databaseInfoPasswordText.getText();

		final DBFluteNewClientPageResult result = new DBFluteNewClientPageResult();
		result.setDatabaseInfoUrl(databaseInfoUrl);
		result.setDatabaseInfoSchema(databaseInfoSchema);
		result.setDatabaseInfoUser(databaseInfoUser);
		result.setDatabaseInfoPassword(databaseInfoPassword);

		return result;
	}
}
