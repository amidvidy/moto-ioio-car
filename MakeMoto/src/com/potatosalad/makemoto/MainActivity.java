package com.potatosalad.makemoto;

import net.sweetmonster.app.AnalogInput;
import net.sweetmonster.app.BaseIOIOLooper;
import net.sweetmonster.app.ConnectionLostException;
import net.sweetmonster.app.DigitalOutput;
import net.sweetmonster.app.IOIOLooper;
import net.sweetmonster.app.PwmOutput;
import net.sweetmonster.app.ioio;
import net.sweetmonster.app.MainActivityPhone.Looper;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends IOIOActivity {

	private Button forwardButton;	
	private Button backwardButton;
	private Button breakButton;
	
	private boolean fwd = false;
	private boolean bwd = false;
	private boolean brk = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		forwardButton = (Button) findViewById(R.id.forwardButton);
		backwardButton = (Button) findViewById(R.id.backButton);
		breakButton = (Button) findViewById(R.id.breakButton);
		
		forwardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// send 1 to 4A (h bridge)
				// send 0 to 3A
				fwd = true;
				bwd = false;
				brk = false;
			}
		});
		backwardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// send 0 to 4A
				// send 1 to 3A
				fwd = false;
				bwd = true;
				brk = false;
			}
		});
		breakButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stu
				// send 1 to 4A
				// send 1 to 3A
				fwd = false;
				bwd = false;
				brk = true;
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 
	
	
	
	// IOIO Stuff!!
		/**
		 * This is the thread on which all the IOIO activity happens. It will be run
		 * every time the application is resumed and aborted when it is paused. The
		 * method setup() will be called right after a connection with the IOIO has
		 * been established (which might happen several times!). Then, loop() will
		 * be called repetitively until the IOIO gets disconnected.
		 */
		class Looper extends BaseIOIOLooper {
			/** The on-board LED. */
			private DigitalOutput led_;
			PwmOutput pwm_;
			private AnalogInput mInput_;
			
			// MotoMover stuff
			private DigitalOutput four_a;
			private DigitalOutput three_a;
			private DigitalOutput four_y;
			private DigitalOutput three_y;

			/**
			 * Called every time a connection with IOIO has been established.
			 * Typically used to open pins.
			 * 
			 * @throws ConnectionLostException
			 *             When IOIO connection is lost.
			 * 
			 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
			 */
			@Override
			protected void setup() throws ConnectionLostException {
				Log.d(TAG, "setup");

				led_ = ioio_.openDigitalOutput(0, true);
				pwm_ = ioio_.openPwmOutput(new DigitalOutput.Spec(5, Mode.NORMAL), 100);
				mInput_ = ioio_.oct(40);
				// sendMessage(25f);
				//processingSketch.setVal(999);
				
				// MotoMover stuff
				four_a = ioio_.openDigitalOutput(1, true);
				four_y = ioio_.openDigitalOutput(2, true);
				three_a = ioio_.openDigitalOutput(3, true);
				three_y = ioio_.openDigitalOutput(4, true);
				

			}

			/**
			 * Called repetitively while the IOIO is connected.
			 * 
			 * @throws ConnectionLostException
			 *             When IOIO connection is lost.
			 * 
			 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
			 */
			@Override
			public void loop() throws ConnectionLostException {
				Log.d(TAG, "loop");
				pwm_.setPulseWidth(500);

				led_.write(true);
				

				try {
					float v = mInput_.read();
					Log.d("qq", "val " + v);
					

				} catch (InterruptedException e) {
					e.printStackTrace();


				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}

		/**
		 * A method to create our IOIO thread.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
		 */
		@Override
		protected IOIOLooper createIOIOLooper() {
			Log.d(TAG, "createIOIOLooper");

			return new Looper();
		}


}
