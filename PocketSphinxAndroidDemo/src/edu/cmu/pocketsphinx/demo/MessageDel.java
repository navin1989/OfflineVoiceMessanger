package edu.cmu.pocketsphinx.demo;

public class MessageDel {
	private String txtBody;
	private String txtSender;
	private int inboxImage;
	
   public MessageDel(String txtBody,String txtSender,int InboxImage){
	   super();
	   this.txtBody=txtBody;
	   this.txtSender=txtSender;
	   this.inboxImage=inboxImage;
   }

public String getTxtBody() {
	return txtBody;
}

public void setTxtBody(String txtBody) {
	this.txtBody = txtBody;
}

public String getTxtSender() {
	return txtSender;
}

public void setTxtSender(String txtSender) {
	this.txtSender = txtSender;
}

public int getInboxImage() {
	return inboxImage;
}

public void setInboxImage(int inboxImage) {
	this.inboxImage = inboxImage;
}
   
}
