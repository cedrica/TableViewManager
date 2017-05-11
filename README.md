# TableViewManager V 4.4.0 
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Annotations](#annotations)
4. [API](#api)
+   [How to use](#howToUseEm)
    - [PoJos annotation](#annotatePojo)  
    - [configuration](#configuration)
5. [Take a look on the result](#enjoyResult)
6. [mvn repository](#mvnRepo)
7. [Releases](#releases)

## 1.   Overview<a name="overview"></a>
ExcelManager is a lib use to generate simple excel-report easily and quikly.

## 2.   Architecture<a name="architecture"></a>
![TableViewManager-Arch](/tableview-manager/images/Architecture.PNG)

## 3.   Annotations<a name="annotations"></a>
```java
@Column (String customname ,int[] bgColor, int[] fgColor, int fontSize, String fontFamily,String parent, 
        boolean isBold,boolean isItalic,boolean isEuroNumber,int columnSize,
        AddGlyphIcon addGlyphIcon,
        Condition[] bgForGivenConditions,
        Condition[] fgForGivenConditions)
this annotation is use to render tableview cells. 

@AddGlyphIcon (FontAwesome.Glyph iconName,boolean beforeText,boolean showDefaultIcon)
Use to add an icon to cell. If beforeText is setted to true if there is a text in the cell the icon will be set before the text oderwise after.
At the moment showDefaultIcon is a worse way to display or not the default icon which is setted to Glyph.TENCENT_WEIBO. Setting showDefaultIcon to false will tell the tablemanager to don´t display that default icon.
But if you parametrize the annotation with the same name as the default one, then you must set showDefaultIcon to true.

@Condition(String ifFieldValue, String thenBackgroundColor)
Use this annotation inside the Column annotation to set the cells background to the given color when matching between ifFieldValue and the cell.

@Transient 
use this annotation to omit fields that you don´t want to display in the table

@Column(formatter=@Formatter(formatterTyp=FormatterTyp.AMOUNT, pattern="###,###.##"), alignement=Alignement.RIGHT)
use this annotation for amount formating
```

## 4.   API
```java

    /**
    *Contructor 
    */
	public TableViewManager(TableView tableView);

    /**
    *Contructor 
    */
	public TableViewManager();
	/**
	 * convert a list of data into an observableList in order to set the
	 * tableViews Items
	 *
	 * @param data
	 */
	public void addData(List<T> data);
	public void clear();

	public void setItems(ObservableList<T> items);
	/**
	 * This function initialize table columns of a POJO. Here the columns name
	 * are represented by the name of the attributes of the POJO
	 *
	 * @param entityClazz:
	 *        the class name of the PoJo
	 */
	public void initColumnAndSetValueFactory(Class entityClazz);

	private void renderColumnCells(TableColumn column);


	public void run(Class clazz, String method, String param);
	/**
	 * assign column children to they corresponding parent
	 * 
	 * @param map:
	 *        key is the parent name and values children´s one
	 */
	private void mergeColumns(HashMap<String, List<Field>> map);

	/**
	 * create column and set factory value
	 * 
	 * @param att:
	 *        field of the Pojos the be map to the column
	 * @param colName:
	 *        column name
	 * @return
	 */
	private TableColumn createColumnAndSetValueFactory(Field att, String colName);

	/**
	 * to assign a custom node to the cells of a given column.
	 *
	 * @param column
	 * @param node
	 * @param direction
	 */
	private void applyGraphic(TableColumn column, Node node);

	/**
	 * render the given column cells with the given Node
	 * 
	 * @param column
	 * @param customNode
	 */
	public void setCustomCellFactory(TableColumn column, Node customNode);

	/**
	 * set the cell factory depending on the given converter. if converter is null the return a new
	 * TextFieldTableCell(new StringConverterHelper());
	 * 
	 * @param tabColumn
	 * @param converterClazz
	 */
	public void setTextFieldTableCellFactory(TableColumn column, Class converterClazz);

	/**
	 * set the cell factory depending on the given converter
	 * 
	 * @param column
	 * @param converterClazz
	 * @param ol:
	 *        list of items
	 */
	public void setComboboxTableCellFactory(TableColumn column, Class converterClazz, ObservableList<T> ol);

	/**
	 * set CheckBoxTableCellFactory
	 * 
	 * @param tabColumn
	 */
	public void setCheckBoxTableCellFactory(TableColumn tabColumn);


	/**
	 * @author ca.leumaleu
	 *         the function is used to update a cell by performing an action when a cell is edited.
	 *         Notice that this function will replace any present cellFactory by a TextFieldCellFactory as follow:
	 *         {@code: column.setCellFactory(TextFieldTableCell.forTableColumn());
	 * @param serviceClazz:
	 *        to access the class where the method to execute is declared
	 * @param method:
	 *        name of the method to execute
	 */
	public void performOnEditCommit(TableColumn column, Class serviceClazz, String method);

	/**
	 * set the selections mode
	 *
	 * @param selectionMode
	 */
	public void setCellSelectionMode(SelectionMode selectionMode);

	public void setCellSelection(boolean b) {
		tableView.getSelectionModel().setCellSelectionEnabled(b);
	}

	/**
	 * hide the table header
	 */
	public void hideTableHeader();

	public TableView<T> getTableView();

	public void setItemsPropertyMethod(ListProperty<T> listProperty);
```
+ enums

|Alignement| Direction |FormatterTyp|CellFactoryTyp|
|----------|-----------|------------|--------------|
|RIGHT     |HORIZONTAL|CURRENCY_STRING_CONVERTER|CHECKBOXTABLECELL|
|LEFT      |VERTICAL|DATE|TEXTFIELDTABLECELL|
|MIDDLE    ||AMOUNT|COMBOBOXTABLECELL|

### 5. How to use<a name="howToUseEm"></a>
Annotate the fields of the PoJo as desired<a name="annotatePojo"></a>
```java
@Column(customname = "my name", bgForGivenConditions = { 
				@Condition(ifFieldValue="name0", thenBackgroundColor="orange;"),
				@Condition(ifFieldValue="name1", thenBackgroundColor="#ffff80;")})
private String name;

@Column(formatter=@Formatter(formatterTyp=FormatterTyp.AMOUNT, pattern="###,###.##"), alignement=Alignement.RIGHT)
private double alt;

//getter and setter are required
```
configure the table<a name="configuration"></a>
```java
List<Person> list = new ArrayList<>();
for(int i = 0; i < 5; i++){
	list.add(new Person("name"+i, i*100000.943));
}
items = FXCollections.observableArrayList(list);
TableViewManager<Person> tvm = new TableViewManager<Person>(table);
tvm.initColumnAndSetValueFactory(Person.class);
table.setItems(items);
table.setTableMenuButtonVisible(true);
```
## Releases<a name="releases"></a>
### Version 4.2.0
New annotation 
isEuroNumber has been replaced by @Formatter.
```java
@Formatter (FormatterTyp formatterTyp, String pattern)
```
## Take a look on the result<a name="enjoyResult"></a>
![TableViewManager-Arch](/TableViewManager/images/table.PNG)
## mvn repository<a name="mvnRepo"></a>
```xml
<dependency>
	<groupId>com.preag.tableviewmanager</groupId>
	<artifactId>TableViewManager</artifactId>
	<version>4.3.0-SNAPSHOT</version>
</dependency>
```
## Releases<a name="releases"></a>
    <version>4.3.0-SNAPSHOT</version> stable
	<version>4.0.0-SNAPSHOT</version> stable
	<version>3.0.0-SNAPSHOT</version> not stable