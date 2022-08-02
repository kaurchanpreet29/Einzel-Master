package com.khana.tse.fiskaly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.khana.tse.fiskaly.transaction.FiskalyPaymentType;
import com.khana.tse.fiskaly.transaction.FiskalyReceiptType;
import com.khana.tse.fiskaly.transaction.FiskalyTransactionController;

public class PosFiskalyController {
	private FiskalyKeyParameter instantParam;
	public FiskalyTransactionController fiskalyController;

	private boolean restartOrder = false;
	private boolean restartReceipt = false;
	private boolean tseEnable = false;



	public PosFiskalyController(boolean tseEnable) {
		this.fiskalyController = new FiskalyTransactionController();
		this.tseEnable = tseEnable;
	}	


	public boolean isRestartReceipt() {
		return restartReceipt;
	}

	public void setRestartReceipt(boolean restartReceipt) {
		this.restartReceipt = restartReceipt;
	}

	private int revNr = 1;

	public boolean isRestartOrder() {
		return restartOrder;
	}

	public void setRestartOrder(boolean restartOrder) {
		this.restartOrder = restartOrder;
	}

	public FiskalyKeyParameter getInstantParam() {
		return instantParam;
	}

	public void setInstantParam(FiskalyKeyParameter instantParam) {
		this.instantParam = instantParam;
	}

	public synchronized FiskalyKeyParameter tseUpdateOrder(TicketItem item, Ticket ticket) {
		if(tseEnable) {
			if(isRestartOrder()) {
				setInstantParam(tseStartOrder(Application.getInstance().getParam()));
				setRestartOrder(false);
				System.out.println("Here ");
			}
			if(isRestartReceipt()) {
				System.out.println("Here ");
				setInstantParam(tseReStartReceipt(getInstantParam(), ticket));
			}
			if(instantParam==null)
				instantParam = Application.getInstance().getParam();
			instantParam.setLatestRevision(revNr);
			revNr += 1;
			return fiskalyController.updateOrder(instantParam, Arrays.asList(item));
		}else
			return null;

	}	

	public synchronized FiskalyKeyParameter tseFinishOrder(List<TicketItem> itemList,FiskalyKeyParameter param ) {
		if(tseEnable) {
			revNr = 1;
			if(!deletedList.isEmpty()&&deletedList.size()>0) {
				itemList.addAll(deletedList);
				deletedList.clear();
			}		
			return fiskalyController.finishOrder(param, itemList);
		}else
			return null;

	}
	
	public synchronized FiskalyKeyParameter tseStartFinishOrder(List<TicketItem> itemList,FiskalyKeyParameter param ) {
		if(tseEnable) {
			revNr = 1;
			if(!deletedList.isEmpty()&&deletedList.size()>0) {
				itemList.addAll(deletedList);
				deletedList.clear();
			}		
			return fiskalyController.finishOrder(param, itemList);
		}else
			return null;

	}
	
	

	public synchronized TSEReceiptData tseFinishReceipt(FiskalyKeyParameter param,List<TicketItem> items, FiskalyPaymentType paymetType, FiskalyReceiptType receiptType  ) {
		if(tseEnable) {
			return fiskalyController.finishReceipt(param, fiskalyController.createReceipt(items, paymetType, receiptType));
		}else
			return null;

	}

	public synchronized FiskalyKeyParameter tseStartOrder(FiskalyKeyParameter param) {
		if(tseEnable) {
			return fiskalyController.startOrder(param);
		}else
			return null;

	}

	public synchronized FiskalyKeyParameter tseStartReceipt(FiskalyKeyParameter param) {
		if(tseEnable) {
			setRestartOrder(false);
			return fiskalyController.startReceipt(param);
		}else
			return null;
	}

	public synchronized FiskalyKeyParameter tseReStartReceipt(FiskalyKeyParameter param, Ticket ticket) {
		if(tseEnable) {
			setRestartOrder(false);
			setRestartReceipt(false);
			param =  fiskalyController.startReceipt(param);
			ticket.setTseClientId(param.getClientId());
			ticket.setTseReceiptTxId(param.getTransactionIdReceipt());
			ticket.setTseReceiptTxRevisionNr(String.valueOf(param.getLatestRevisionReceipt()));
			return param;
		}else
			return null;
	}

	/*
	 * CancelReceiptShouldBePrinted or not?
	 */
	public synchronized void tseInstantCancelOrder(FiskalyKeyParameter param) {
		if(tseEnable) {
			try {
				fiskalyController.cancelReceipt(param, fiskalyController.createReceipt(null, FiskalyPaymentType.CASH, FiskalyReceiptType.CANCELLATION));
			}catch(Exception ex) {
				System.out.println("Cancel failure "+ex.getMessage());
			}
		}
	}
	
	
	
	
	
	public synchronized void tsetCancelOrder(FiskalyKeyParameter param, Ticket ticket) {
		if(tseEnable) {
			try {
				TSEReceiptData data = fiskalyController.cancelReceipt(param, fiskalyController.createReceipt(ticket.getTicketItems(), FiskalyPaymentType.CASH, FiskalyReceiptType.CANCELLATION));
				if(data!=null)
					TSEReceiptDataDAO.getInstance().saveOrUpdate(data);

				if (data != null) {
					ticket.setTseReceiptDataId(data.getId());
					ticket.setTseReceiptTxRevisionNr(data.getLatestRevision());
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
			}catch(Exception ex) {
				System.out.println("Cancel failure "+ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	


	public synchronized void startOrderReceipt(Ticket ticket) {		
		if(tseEnable) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					setInstantParam(tseStartOrder(Application.getInstance().getParam()));
					setInstantParam(tseStartReceipt(getInstantParam()));
					ticket.setTseClientId(getInstantParam().getClientId());
					ticket.setTseReceiptTxId(getInstantParam().getTransactionIdReceipt());
					System.out.println(String.valueOf(getInstantParam().getLatestRevisionReceipt()));
					ticket.setTseReceiptTxRevisionNr(String.valueOf(getInstantParam().getLatestRevisionReceipt()));
				}

			});
			t.start();
		}
	}

	List<TicketItem> queListtt = new ArrayList<TicketItem>();

	public synchronized void addQue(TicketItem item, Ticket ticket) {
		System.out.println("Start Order");
		if(tseEnable) {
			if(queListtt.isEmpty()&&queListtt!=null) {
				queListtt.add(item);
				while(!queListtt.isEmpty()) {
					for(TicketItem itmmm:queListtt) {
						queListtt.remove(itmmm);
						queListtt.clear();
						try {
							updateOrder(itmmm, ticket);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						break;
					}				
				}
			}else {
				queListtt.add(item);
				System.out.println("added");
			}
		}
	}


	public synchronized void addQue1(MenuItem mItem, int count, Ticket ticket) {
		if(tseEnable) {
			TicketItem item = new TicketItem();
			item.setUnitPrice(mItem.getPrice());
			item.setItemCount(count);
			item.setName(mItem.getName());
			if(queListtt.isEmpty()&&queListtt!=null) {
				queListtt.add(item);
				while(!queListtt.isEmpty()) {
					for(TicketItem itmmm:queListtt) {
						queListtt.remove(itmmm);
						queListtt.clear();
						try {
							updateOrder(itmmm, ticket);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						break;
					}				
				}
			}else {
				queListtt.add(item);
			}
		}

	}

	public synchronized void addQueType2(String name, Double price, int count, Ticket ticket) {		
		if(tseEnable) {
			TicketItem item = new TicketItem();			
			item.setUnitPrice(price);
			item.setItemCount(count);
			item.setName(name);		
			if(queListtt.isEmpty()&&queListtt!=null) {
				queListtt.add(item);
				while(!queListtt.isEmpty()) {
					for(TicketItem itmmm:queListtt) {
						queListtt.remove(itmmm);
						queListtt.clear();
						try {
							updateOrder(itmmm, ticket);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						break;
					}				
				}
			}else {
				queListtt.add(item);
			}
		}

	}

	public synchronized void updateOrder(TicketItem item, Ticket ticket) {
		if(tseEnable) {
			Thread updater = new Thread(new Runnable() {
				@Override
				public void run() {
					tseUpdateOrder(item, ticket);			
				}

			});
			updater.start();
		}
	}	

	List<TicketItem> deletedList = Collections.synchronizedList(new ArrayList<TicketItem>());	

	public synchronized void cancelItem(TicketItem itemm, boolean add, String delReason) {
		if(tseEnable) {
			if(add) {
				TicketItem item = new TicketItem();	
				item.setUnitPrice(itemm.getUnitPrice());		
				item.setItemCount(itemm.getItemCount());
				item.setName(itemm.getName());
				deletedList.add(item);
			}				
			TicketItem item = new TicketItem();	
			item.setUnitPrice(0-itemm.getUnitPrice());		
			item.setItemCount(itemm.getItemCount());
			if(delReason.isEmpty())
				item.setName(POSConstants.CANCEL);
			else if(delReason.equals(POSConstants.ARTICKEL_MOVE))
				item.setName(POSConstants.ARTICKEL_MOVE);
			else if(delReason.equals(POSConstants.ARTICKEL_INSTANT_CANCEL))
				item.setName(POSConstants.ARTICKEL_INSTANT_CANCEL);
			else
				item.setName(POSConstants.ARTICKEL_MOVE);
			deletedList.add(item);
		}

	}


	public synchronized void cancelCompleteOrder(Ticket ticket, List<TicketItem> itemList, boolean restartReceipt, boolean angebot) {
		if(tseEnable) {
			try {
						FiskalyKeyParameter delParam = null;
						if(restartReceipt)
							delParam = fiskalyController.startOrder(Application.getInstance().getParam());
						else
							delParam = getInstantParam();
						delParam = fiskalyController.finishOrder(delParam, getCanceldItemList(itemList, angebot));
						if(!restartReceipt) {
							delParam.setTransactionIdReceipt(ticket.getTseReceiptTxId());
							delParam.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
						}else {
							delParam = tseReStartReceipt(delParam, ticket);
						}
						delParam.setTransactionIdReceipt(ticket.getTseReceiptTxId());
						delParam.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
						tseInstantCancelOrder(delParam);
					
				
			}catch(Exception ex) {

			}
		}

	}
	
	
	
	public synchronized void cancelPrevOrder(Ticket ticket, List<TicketItem> itemList, boolean restartReceipt, boolean angebot) {
		if(tseEnable) {
			try {
						FiskalyKeyParameter delParam = null;
						if(restartReceipt)
							delParam = fiskalyController.startOrder(Application.getInstance().getParam());
						else
							delParam = getInstantParam();
						delParam = fiskalyController.finishOrder(delParam, getCanceldItemList(itemList, angebot));
						if(!restartReceipt) {
							delParam.setTransactionIdReceipt(ticket.getTseReceiptTxId());
							delParam.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
						}else {
							delParam = tseReStartReceipt(delParam, ticket);
						}
						delParam.setTransactionIdReceipt(ticket.getTseReceiptTxId());
						delParam.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
						tsetCancelOrder(delParam, ticket);
					
				
			}catch(Exception ex) {

			}
		}

	}
	
	
	
	

	public synchronized List<TicketItem> getCanceldItemList(List<TicketItem> itemList, boolean angebot) {
		if(tseEnable) {
			List<TicketItem> delList = new ArrayList<TicketItem>();
			for(TicketItem itemm : itemList) {
				delList.add(itemm);
				TicketItem item = new TicketItem();	
				item.setUnitPrice(0-itemm.getUnitPrice());		
				item.setItemCount(itemm.getItemCount());
				if(angebot)
					item.setName(POSConstants.OFFER);
				else
					item.setName(POSConstants.ARTICKEL_CANCEL);
				delList.add(item);
			}
			return delList;
		}else
			return null;
	}
}
