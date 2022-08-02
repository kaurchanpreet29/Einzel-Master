package com.khana.gross.handle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.IntFunction;

import com.floreantpos.model.Customer;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.UserDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import com.khana.gross.handle.GrossHandelController.OrderInfo;
import com.khana.gross.handle.TestTableView.Record;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class GrossHandelController implements Initializable {
	private static ObservableList<OrderInfo> orders;

	//controls	
	//		@FXML
	//	   private Label btnAddItem;
	//	   @FXML
	//	   private Label btnBarcode;
	//	   @FXML
	//	   private Label btnAddCustomer;
	//	   @FXML
	//	   private Label btnCustomer;
	//	   @FXML
	//	   private Label btnLocation;
	//	   @FXML
	//	   private Label btnInfo;
	@FXML
	private Button btnAddItem;
	@FXML
	private Button btnBarcode;
	@FXML
	private Button btnAddCustomer;
	@FXML
	private Button btnCustomer;
	@FXML
	private Button btnLocation;
	@FXML
	private Button btnInfo;

	//tableView

	@FXML
	private TableView<OrderInfo> tableView;

	@FXML
	private TableColumn<OrderInfo, Integer> idCol;

	@FXML
	private TableColumn<OrderInfo, String> productCol;

	@FXML
	private TableColumn<OrderInfo, Integer> quantityCol;

	@FXML
	private TableColumn<OrderInfo, Double> nettoCol;

	@FXML
	private TableColumn<OrderInfo, Double> bruttoCol;	
	
	@FXML
	private TableColumn<OrderInfo, Double> subtotalCol;	

	@FXML
	private TableColumn<OrderInfo, OrderInfo> colDelete;

	@FXML
	private ComboBox<Customer> searchCustomer;
	
	@FXML
	private ComboBox<String> location;
	
	@FXML
	private TextField itemSearch;

	private ObservableList<Customer> userList = FXCollections.observableArrayList();
	 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orders = FXCollections.observableArrayList();
		initCol();
		tableView.getItems().addAll(orders);
		tableView.setEditable(true);
		
		btnAddItem.setOnAction(e -> increaseCount());
		btnBarcode.setOnAction(e -> increaseCount());
		btnAddCustomer.setOnAction(e -> increaseCount());
		btnInfo.setOnAction(e -> increaseCount());
		Customer user = new Customer();
		user.setName("Jyoti");
		user.setAutoId(0);
		userList.add(user);
		Customer user1 = new Customer();
		user1.setName("Anji");
		user1.setAutoId(1);
		userList.add(user1);
		Customer user2 = new Customer();
		user2.setAutoId(2);
		user2.setName("Suresh");
		userList.add(user2); 
		searchCustomer.setItems(userList);
		FxAutoCompleteUtil.autoCompleteComboBoxPlus(searchCustomer, (typedText, itemToCompare) -> itemToCompare.getName().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.getAutoId().toString().equals(typedText));
	}
	int count =1;
	public void increaseCount() {
		orders.add(new OrderInfo(count, "MyNAME", count, 0.0, 2.0, 2)); 
		count++;
		updateView();
	}

	public void updateView() {
		tableView.getItems().removeAll(orders);
		tableView.getItems().addAll(orders);
		tableView.refresh();
	}

	List<TicketItem> currentList = new ArrayList<TicketItem>();
	/*private void addTicketItem(TicketItem item) {       
		try {               
			orders.add(new OrderInfo(item.getItemId(), item.getName(), item.getItemCount(), item.getUnitPriceDisplay(), item.getUnitPriceDisplay()));                
			updateView();
			currentList.add(item);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void removeTicketItem(TicketItem item) {       
		try {               
			orders.add(new OrderInfo(item.getItemId(), item.getName(), item.getItemCount(), item.getUnitPriceDisplay(), item.getUnitPriceDisplay()));                
			updateView();
			currentList.add(item);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

	public void setDataFromDB() {

//		try {
//			orders = FXCollections.observableArrayList();
//			initCol();
//				 	     btnAddItem.setOnAction(e -> increaseCount());
//				 	     btnBarcode.setOnAction(e -> increaseCount());
//				 	     btnAddCustomer.setOnAction(e -> increaseCount());
//				 	     btnInfo.setOnAction(e -> increaseCount());
//
//			List<User> uList = new ArrayList<>();
//			User user = new User();
//			user.setFirstName("Jyoti");
//			uList.add(user);
//			user = new User();
//			user.setFirstName("Anji");
//			uList.add(user);
//			user = new User();
//			user.setFirstName("Suresh");
//			uList.add(user);    
//
//				 	     cmbSearchCustomer = new ComboBox<User>();	 
//				 	    	 cmbSearchCustomer.setItems(FXCollections.observableArrayList(uList));
//
//
//			//	 	     FxAutoCompleteUtil.autoCompleteComboBoxPlus(cmbSearchCustomer, (typedText, itemToCompare) -> itemToCompare.getFirstName().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.getAutoId().toString().equals(typedText));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

	}


	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		 Callback<TableColumn, TableCell> cellFactory =
		            new Callback<TableColumn, TableCell>() {
		                public TableCell call(TableColumn p) {
		                    return new EditingCell();
		                }
		            };
		           
//		quantityCol.setOnEditCommit(
//	              new EventHandler<TableColumn.CellEditEvent<OrderInfo, Integer>>() {
//	                  @Override public void handle(TableColumn.CellEditEvent<OrderInfo, Integer> t) {
//	                      ((OrderInfo)t.getTableView().getItems().get(
//	                              t.getTablePosition().getRow())).setQuantity((t.getNewValue()));
//	                  }
//	              });
		
		nettoCol.setCellValueFactory(new PropertyValueFactory<OrderInfo, Double>("netto"));
		bruttoCol.setCellValueFactory(new PropertyValueFactory<OrderInfo, Double>("brutto"));
		subtotalCol.setCellValueFactory(new PropertyValueFactory<OrderInfo, Double>("subtotal"));
		colDelete.setCellValueFactory(
				param -> new ReadOnlyObjectWrapper<>(param.getValue())
				);
		colDelete.setCellFactory(param -> new TableCell<OrderInfo, OrderInfo>() {
			private final Button deleteButton = new Button("delete");

			@Override
			protected void updateItem(OrderInfo person, boolean empty) {
				super.updateItem(person, empty);
				if (person == null) {
					setGraphic(null);
					return;
				}
				setGraphic(deleteButton);
				deleteButton.setOnAction(
						event -> getTableView().getItems().remove(person)
						);
			}
		});


	}
	
	public void edit(TableColumn.CellEditEvent<OrderInfo, Integer> t) {
		((OrderInfo)t.getTableView().getItems().get(
                t.getTablePosition().getRow())).setQuantity((t.getNewValue()));
	}
	
	
	public class StateTextFieldTableCell<S, T> extends TextFieldTableCell<S, T> {

	    private final IntFunction<ObservableValue<Boolean>> editableExtractor;

	    public StateTextFieldTableCell(IntFunction<ObservableValue<Boolean>> editableExtractor, StringConverter<T> converter) {
	        super(converter);
	        this.editableExtractor = editableExtractor;
	    }

	    @Override
	    public void updateIndex(int i) {
	        super.updateIndex(i);
	        if (i == -1)  {
	            editableProperty().unbind();
	        } else {
	            editableProperty().bind(editableExtractor.apply(i));
	        }
	    }

	    public <U, V> Callback<TableColumn<U, V>, TableCell<U, V>> forTableColumn(
	            IntFunction<ObservableValue<Boolean>> editableExtractor,
	            StringConverter<V> converter) {
	        return column -> new StateTextFieldTableCell<>(editableExtractor, converter);
	    }

	    public <U> Callback<TableColumn<U, String>, TableCell<U, String>> forTableColumn(
	            IntFunction<ObservableValue<Boolean>> editableExtractor) {
	        return forTableColumn(editableExtractor, new DefaultStringConverter());
	    }

	}
	

	public class OrderInfo {
		private SimpleIntegerProperty id;
		private SimpleStringProperty product;
		private SimpleDoubleProperty subtotal;
		private SimpleDoubleProperty netto;
		private SimpleIntegerProperty quantity;
		
		
		public void setId(Integer idVal) {
			id.set(idVal);
		}


		public void setProduct(String productVal) {
			product.set(productVal);
		}


		public void setSubtotal(Double subtotalVal) {
			subtotal.set(subtotalVal);
		}


		public void setNetto(Double nettoVal) {
			netto.set(nettoVal);
		}


		public void setQuantity(Integer quantity1) {
			quantity.set(quantity1);
		}


		public void setBrutto(SimpleDoubleProperty brutto) {
			this.brutto = brutto;
		}

		private SimpleDoubleProperty brutto;


		public OrderInfo(int id, String productName, int count, double priceIncTax, double bruttoAmnt, double subtotal) {
			this.product = new SimpleStringProperty(productName);
			this.netto = new SimpleDoubleProperty(priceIncTax);
			this.subtotal = new SimpleDoubleProperty(count*bruttoAmnt);
			this.quantity = new SimpleIntegerProperty(count);
			this.brutto = new SimpleDoubleProperty(bruttoAmnt);
			this.id = new SimpleIntegerProperty(id);
		}


		public String getProduct() {
			return product.get();
		}

		public SimpleStringProperty productProperty() {
			return product;
		}

		public double getNetto() {
			return netto.get();
		}

		public SimpleDoubleProperty nettoProperty() {
			return netto;
		}

		public double getSubtotal() {
			return subtotal.get();
		}

		public SimpleDoubleProperty subtotalProperty() {
			return subtotal;
		}
		
		public double getBrutto() {
			return brutto.get();
		}

		public SimpleDoubleProperty bruttoProperty() {
			return brutto;
		}

		public int getQuantity() {
			return quantity.get();
		}

		public SimpleIntegerProperty quantityProperty() {
			return quantity;
		}

		public int getId() {
			return id.get();
		}

		public SimpleIntegerProperty idProperty() {
			return id;
		}

	}
	
	private List<TicketItem> getTicketItems(){
		List<TicketItem> itemList = new ArrayList<>();
		return itemList;
	}
	
	class EditingCell extends TableCell<OrderInfo, Integer> {
		 
	    private TextField textField;
	   
	    public EditingCell() {}
	   
	    @Override
	    public void startEdit() {
	        super.startEdit();
	       
	        if (textField == null) {
	            createTextField();
	        }
	       
	        setGraphic(textField);
	        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	        textField.selectAll();
	    }
	   
	    @Override
	    public void cancelEdit() {
	        super.cancelEdit();
	       
	        setText(String.valueOf(getItem()));
	        setContentDisplay(ContentDisplay.TEXT_ONLY);
	    }

	    @Override
	    public void updateItem(Integer item, boolean empty) {
	        super.updateItem(item, empty);
	       
	        if (empty) {
	            setText(null);
	            setGraphic(null);
	        } else {
	            if (isEditing()) {
	                if (textField != null) {
	                    textField.setText(getString());
	                }
	                setGraphic(textField);
	                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            } else {
	                setText(getString());
	                setContentDisplay(ContentDisplay.TEXT_ONLY);
	            }
	        }
	    }

	    private void createTextField() {
	        textField = new TextField(getString());
	        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
	        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	           
	            @Override
	            public void handle(KeyEvent t) {
	                if (t.getCode() == KeyCode.ENTER) {
	                    commitEdit(Integer.parseInt(textField.getText()));
	                } else if (t.getCode() == KeyCode.ESCAPE) {
	                    cancelEdit();
	                }
	            }
	        });
	    }
	   
	    private String getString() {
	        return getItem() == null ? "" : getItem().toString();
	    }

	}
}
