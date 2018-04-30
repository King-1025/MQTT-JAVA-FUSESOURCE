package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

public class StatusShowPanel extends JPanel implements ActionListener{

	private JScrollPane jsp;
	private JList jl;
	private JLabel totalInfo;
	private JButton clear;
	private int count;
	private int maxSize=50;
	private String other[]= {"运行状态","无消息","清空"};
	public StatusShowPanel() {
		// TODO Auto-generated constructor stub
		this(400,505);
	}
	public StatusShowPanel(int width,int height) {
		this.setSize(width,height);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel head=new JPanel();
	    JPanel body=new JPanel();
	    head.setLayout(new FlowLayout(FlowLayout.CENTER));
	    body.setLayout(null);
	    jsp=new JScrollPane();
	    jl=new JList();
	    jl.setSelectionBackground(Color.GREEN);
	    jsp.setViewportView(jl);
	    jsp.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JPanel jp=new JPanel();
		totalInfo=new JLabel(other[1]);
		clear=new JButton(other[2]);
		clear.addActionListener(this);
		clear.setBackground(Color.LIGHT_GRAY);
		clear.setVisible(false);
		jp.setBackground(Color.GRAY);
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));
		jp.add(totalInfo);
		jp.add(clear);
		jsp.setBounds(0, 0, width, height*7/8);
		jp.setBounds(0, height*7/8, width, height/8);
	    body.add(jsp);
	    body.add(jp);
	    head.add(new JLabel(other[0]));
		head.setBackground(Color.GRAY);
		this.add(head,BorderLayout.NORTH);
		this.add(body,BorderLayout.CENTER);
	}
	
	public void updateStatus(String info) {
		int size=jl.getModel().getSize();
		DefaultListModel listModel=null;
		if(size==0) {
			listModel=new DefaultListModel();
			listModel.addElement(info);
		}else if(size<maxSize) {
			listModel=(DefaultListModel) jl.getModel();
			listModel.addElement(info);
		}else {
			listModel=(DefaultListModel) jl.getModel();
			listModel.removeRange(0, maxSize/2);
			listModel.addElement(info);
		}
		count++;
		jl.setModel(listModel);
		size=jl.getModel().getSize();
		jl.setSelectedIndex(size-1);
		
		jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());

		totalInfo.setText("总接收:"+count+"条,可显示:"+size+"条");
		if(!clear.isVisible())
		{clear.setVisible(true);}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(clear==arg0.getSource()) {
			ListModel listModel=jl.getModel();
			if(listModel.getSize()>0) {
				((DefaultListModel)listModel).removeAllElements();
				totalInfo.setText(other[1]);
				totalInfo.setText("总接收:"+count+"条,可显示:0条");
				if(clear.isVisible())
				{clear.setVisible(false);}
			}
		}
	}
}
