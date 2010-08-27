package com.rapidftr.screens;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controls.TitleField;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.utilities.BoldRichTextField;
import com.rapidftr.utilities.ChildFieldIgnoreList;
import com.rapidftr.utilities.ImageUtility;

public class ViewChildScreen extends CustomScreen {

	Child child;

	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		this.child = child;
		clearFields();
		add(new TitleField());
		add(new LabelField("Child Details"));
		add(new SeparatorField());
		renderChildFields(child);

	}

	private void renderChildFields(Child child) {
		updateChildFields(child);
		Hashtable data = child.getKeyMap();
		HorizontalFieldManager hmanager = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLLBAR);
		renderBitmap(data, hmanager);

		hmanager.add(new BoldRichTextField("   " + data.get(new String("name"))));
		add(hmanager);
		RichTextField richField[] = new RichTextField[data.size()];
		int i = 0;

		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) 
		{
			String key = (String) keyList.nextElement();
			String value = (String) data.get(key);
			if(ChildFieldIgnoreList.isInIgnoreList(key))
			{
				continue;
			}
			key = key.replace('_', ' ');
			richField[i] = BoldRichTextField.getSemiBoldRichTextField(key
					+ " :", value);

			add(richField[i]);
			add(new SeparatorField());
			i++;

		}

	}

	private void updateChildFields(Child child) {
		FormStore fstore = new FormStore();
		Vector forms = fstore.getForms();

		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			for(Enumeration fields = form.getFieldList().elements(); fields.hasMoreElements();)
			{
					FormField field = (FormField) fields.nextElement();
					child.updateField(field.getName());
			}
			    
		}
	}

	private void renderBitmap(Hashtable data, HorizontalFieldManager manager) {

		manager.setMargin(10, 10, 10, 10);

		String ImagePath = "res/default.jpg";
		Bitmap image = Bitmap.getBitmapResource(ImagePath);

		if (ImagePath == null || ImagePath.equals("")) {
			ImagePath = "res/default.jpg";
		} else {
			ImagePath = "file://"
					+ (String) data.get(new String("current_photo_key"));
			FileConnection fconn;

			try {
				fconn = (FileConnection) Connector.open(ImagePath,
						Connector.READ);
				if (fconn.exists()) {
					byte[] imageBytes = new byte[(int) fconn.fileSize()];
					InputStream inStream = fconn.openInputStream();
					inStream.read(imageBytes);
					inStream.close();
					EncodedImage eimg = EncodedImage.createEncodedImage(
							imageBytes, 0, (int) fconn.fileSize());
					image = eimg.getBitmap();
					fconn.close();

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// BitmapField
		Bitmap resize = ImageUtility.resizeBitmap(image, 70, 70);
		BitmapField bf = new BitmapField(resize, BitmapField.FOCUSABLE);

		manager.add(bf);

	}

	private void clearFields() {
		int fieldCount = this.getFieldCount();
		if (fieldCount > 0)
			this.deleteRange(0, fieldCount);
	}

	public void setUp() {
		// TODO Auto-generated method stub

	}

	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	protected void makeMenu(Menu menu, int instance) {
		MenuItem editChildMenu = new MenuItem("Edit Child Detail", 1, 1) {
			public void run() {
				//Move from edit screen directly to the main menu application screen
				controller.popScreen();
				controller.dispatcher().editChild(child);
			}
		};
		menu.add(editChildMenu);
	}

}
