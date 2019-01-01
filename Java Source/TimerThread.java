/*
*	TimerThread.java
*	Date of creation: Dec 22, 2018
*	Date of last modification: Dec 22, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
    TimerThread.java is an extension of the Thread class that runs in parallel with the main PlayChess program.
    A JLabel in the format of a timer starting at zero is given which TimerThread changes to simulate a clock.
*/

import javax.swing.JLabel;


public class TimerThread extends Thread {

    private JLabel T;


    public TimerThread(JLabel T) {
        this.T = T;
    }


    //The run method executes in parallel to update the clock label while play occurs.
    public void run() {
        char[] text = T.getText().toCharArray();

        try {
            while (true) {
                Thread.sleep(1000);
                if (text[6] != '9') {
                    text[6] += 1;
                } else if (text[5] != '5') {
                    text[5] += 1;
                    text[6] = '0';
                } else if (text[3] != '9') {
                    text[3] += 1;
                    text[5] = text[6] = '0';
                } else if (text[2] != '5') {
                    text[2] += 1;
                    text[3] = text[5] = text[6] = '0';
                } else {
                    text[0] += 1;
                    text[2] = text[3] = text[5] = text[6] = '0';
                }

                T.setText(new String(text));
            }
        } catch (InterruptedException e) {
            System.out.println("Timing thread error, quitting application...");
            System.exit(-1);
        }
    }
    
}