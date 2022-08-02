/*
 * TaxEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.dao.GutscheinDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DateTimePicker;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public class GutscheinForm extends BeanEditor<Gutschein> {

  private static final long serialVersionUID = 1L;

  public GutscheinForm() {
    this(new Gutschein());
  }

  public GutscheinForm(Gutschein gutschein) {
    initComponents();

    setBean(gutschein);
  }

  private void initComponents() {

    setLayout(new MigLayout("", "[][grow]", ""));
    lblBarcode = new javax.swing.JLabel("Barcode");
    tfBarCode = new JTextField(30);
    btnGenerateBarCode = new JButton("Generieren");
    if(StringUtils.isNotEmpty(POSConstants.Generieren))
    	btnGenerateBarCode.setText(POSConstants.Generieren);
    
    btnPrint = new JButton("Druck");

    lblValue = new JLabel("Gesamt(EUR)");
    if(StringUtils.isNotEmpty(POSConstants.TOTAL))
    	lblValue.setText(POSConstants.TOTAL +"(EUR)");
	 
    
    tfValue = new JTextField(30);

    lblExpiryDate = new javax.swing.JLabel("Endlich Datum");
    if(StringUtils.isNotEmpty(POSConstants.Endlich_Datum))
    	lblExpiryDate.setText(POSConstants.Endlich_Datum);
    
    dpExpiryDate = new DateTimePicker();

    cbUnlimited = new JCheckBox("Unendlich");
    if(StringUtils.isNotEmpty(POSConstants.Unendlich))
    	cbUnlimited.setText(POSConstants.Unendlich);
    
    cbUnlimited.setBackground(new Color(209, 222, 235));
    cbUnlimited.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if(cbUnlimited.isSelected()) {
          SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
          Date date = null;
          try {
            date = df.parse("31.12.2030");
          } catch (ParseException e1) {
            e1.printStackTrace();
          }
          dpExpiryDate.setDate(date);
        } else {
          dpExpiryDate.setDate(new Date());
        }
      }
      
    });
    
    cbClosed = new JCheckBox("Abgeschlossen");
    if(StringUtils.isNotEmpty(POSConstants.Abgeschlossen))
    	cbClosed.setText(POSConstants.Abgeschlossen);
    
    cbClosed.setBackground(new Color(209, 222, 235));

    btnGenerateBarCode.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        int randomNumber = 0;
        while (true) {
          Random randomGenerator = new Random();
          randomNumber = randomGenerator.nextInt(2147483640);
          if (randomNumber > 0) {
            if (GutscheinDAO.getInstance().findByBarcode(randomNumber + "")== null)
              break;
          }
        }
        tfBarCode.setText(randomNumber + "");
      }

    });

    btnPrint.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        if (tfBarCode.getText().length() <= 0) {
          POSMessageDialog.showError("Barcode ist ungueltig");
          return;
        }
        Date date = dpExpiryDate.getDate();
        if(date == null) {
          POSMessageDialog.showError("Bitte waehlen Sie endlich datum");
          return;
        }
        try {
          Double itemPrice = Double.parseDouble(tfValue.getText().replaceAll(",", "."));
          String price = NumberUtil.formatNumber(itemPrice);
          Code128Bean code128 = new Code128Bean();
          code128.setHeight(15f);
          code128.setModuleWidth(0.3);
          code128.setQuietZone(10);
          code128.doQuietZone(true);

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos,
              "image/x-png", 300, Image.SCALE_DEFAULT, false);
          code128.generateBarcode(canvas, tfBarCode.getText());
          canvas.finish();
          InputStream in = new ByteArrayInputStream(baos.toByteArray());
          HashMap map = new HashMap();
          map.put("price", price + " €");
          map.put("restaurantName", RestaurantDAO.getRestaurant().getName());
          map.put("barcode", in);
          SimpleDateFormat df =new SimpleDateFormat("dd.MM.yyyy");
          map.put("validity", "Gueltig bis "+ df.format(date));
          
          String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/gutscheinBarcode.jasper";

          JasperPrint jasperPrint = JReportPrintService.createJasperPrint(
              FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
          if (jasperPrint != null)
            jasperPrint.setName("barcode:" + tfBarCode.getText());
          jasperPrint.setProperty("printerName", Application.getPrinters()
              .getReceiptPrinter());
          JReportPrintService.printQuitely(jasperPrint);
        } catch (Exception ex) {
          POSMessageDialog.showError("Preis ist ungueltig");
          ex.printStackTrace();
        }
      }
    });

    setBackground(new Color(209, 222, 235));
    
    add(lblValue);
    add(tfValue, "growx,wrap");

    add(lblBarcode);
    add(tfBarCode,"growx");
    add(btnGenerateBarCode, "wrap");

    add(lblExpiryDate);
    add(dpExpiryDate,"growx");
    add(cbUnlimited);
    add(btnPrint, "wrap");

    add(cbClosed);
  }

  private javax.swing.JLabel lblBarcode;
  private javax.swing.JLabel lblExpiryDate;
  private JTextField tfBarCode;
  private JTextField tfValue;
  private javax.swing.JLabel lblValue;

  private JXDatePicker dpExpiryDate;

  private JCheckBox cbUnlimited;
  private JCheckBox cbClosed;

  private JButton btnGenerateBarCode;
  private JButton btnPrint;

  @Override
  public boolean save() {

    try {
      if (!updateModel())
        return false;

      Gutschein gutschein = (Gutschein) getBean();
      GutscheinDAO.getInstance().saveOrUpdate(gutschein);
    } catch (Exception e) {
      MessageDialog.showError(e);
      return false;
    }

    return true;
  }

  @Override
  protected void updateView() {
    Gutschein gutschein = (Gutschein) getBean();
    if (gutschein.getExpiryDate() != null) {
      dpExpiryDate.setDate(gutschein.getExpiryDate());
    } else {
      dpExpiryDate.setDate(new Date());
    }

    if (gutschein.getBarcode() != null) {
      tfBarCode.setText(gutschein.getBarcode());
    }

    if (gutschein.isUnlimited() != null) {
      cbUnlimited.setSelected(gutschein.isUnlimited());
    }

    if (gutschein.isClosed() != null) {
      cbClosed.setSelected(gutschein.isClosed());
    }

    if (gutschein.getValue() != null) {
      tfValue.setText(NumberUtil.formatNumber(gutschein.getValue()));
    }
  }

  @Override
  protected boolean updateModel() {
    Gutschein gutschein = (Gutschein) getBean();

    try {
      Double value = Double.valueOf(tfValue.getText().replaceAll(",", "."));

      gutschein.setBarcode(tfBarCode.getText());
      gutschein.setExpiryDate(dpExpiryDate.getDate());
      gutschein.setValue(value);
      gutschein.setUnlimited(cbUnlimited.isSelected());
      gutschein.setClosed(cbClosed.isSelected());
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

  public String getDisplayText() {
    Gutschein gutschein = (Gutschein) getBean();
    if (gutschein.getId() == null) {
      return "Neu Gutschein";
    }
    return "Gutschein Bearbeiten:" + gutschein.getId();
  }
}
