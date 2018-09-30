package com.example.user.healthymate;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.healthymate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DeviceControlActivity extends Activity implements BluetoothServiceUiLIstner
{
	private final static String TAG = DeviceControlActivity.class.getSimpleName();
	
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	
	private TextView mConnectionState, mDataField, mBPStatus;
	private String mDeviceAddress;
	private ExpandableListView mGattServicesList;
	private BluetoothLeService mBluetoothLeService;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
			new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private boolean mConnected = false;
	private BluetoothGattCharacteristic mNotifyCharacteristic;
	
	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	
	
	private final ServiceConnection mServiceConnection = new ServiceConnection()
	{
		
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service)
		{
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			mBluetoothLeService.setBluetoothServiceUiLIstner(DeviceControlActivity.this);
			if (!mBluetoothLeService.initialize())
			{
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			//ac
			mBluetoothLeService.connect(mDeviceAddress);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName componentName)
		{
			mBluetoothLeService = null;
		}
	};
	
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))
			{
				mConnected = true;
				updateConnectionState(R.string.connected);
				invalidateOptionsMenu();
			}
			else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action))
			{
				mConnected = false;
				updateConnectionState(R.string.disconnected);
				invalidateOptionsMenu();
				clearUI();
			}
			else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
			{
				
				displayGattServices(mBluetoothLeService.getSupportedGattServices());
			}
			else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))
			{
				displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			}
		}
	};
	
	
	private final ExpandableListView.OnChildClickListener servicesListClickListner =
			new ExpandableListView.OnChildClickListener()
			{
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
				                            int childPosition, long id)
				{
					if (mGattCharacteristics != null)
					{
						final BluetoothGattCharacteristic characteristic =
								mGattCharacteristics.get(groupPosition).get(childPosition);
						final int charaProp = characteristic.getProperties();
						if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0)
						{
							
							if (mNotifyCharacteristic != null)
							{
								mBluetoothLeService.setCharacteristicNotification(
										mNotifyCharacteristic, false);
								mNotifyCharacteristic = null;
							}
							mBluetoothLeService.readCharacteristic(characteristic);
						}
						if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)
						{
							mNotifyCharacteristic = characteristic;
							mBluetoothLeService.setCharacteristicNotification(
									characteristic, true);
						}
						return true;
					}
					return false;
				}
			};
	
	private void clearUI()
	{
		mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
		mDataField.setText(R.string.no_data);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gatt_services_characteristics);
		
		final Intent intent = getIntent();
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
		
		
		mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
		mGattServicesList.setOnChildClickListener(servicesListClickListner);
		mConnectionState = (TextView) findViewById(R.id.connection_state);
		mDataField = (TextView) findViewById(R.id.data_value);
		mBPStatus = (TextView) findViewById(R.id.bp_status);
		
		
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null)
		{
			final boolean result = mBluetoothLeService.connect(mDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(mServiceConnection);
		mBluetoothLeService = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.gatt_services, menu);
		if (mConnected)
		{
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
		}
		else
		{
			menu.findItem(R.id.menu_connect).setVisible(true);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_connect:
				mBluetoothLeService.connect(mDeviceAddress);
				return true;
			case R.id.menu_disconnect:
				mBluetoothLeService.disconnect();
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void updateConnectionState(final int resourceId)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionState.setText(resourceId);
			}
		});
	}
	
	/**
	 * Convert and Display the BLOOD PRESSURE
	 *
	 * @param data
	 */
	private void displayData(final String data)
	{
		if (data != null)
		{
			
			// get the user info
			HealthyMate.mUsersReference.child(HealthyMate.mFirebaseUser.getUid())
					.addListenerForSingleValueEvent(new ValueEventListener()
					{
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot)
						{
							
							// cast to user object
							User user = dataSnapshot.getValue(User.class);
							
							if (user != null)
							{
								
								DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
								
								Date date = new Date();
								
								try
								{
									Date date1 = dFormat.parse(user.dob);
									Date date2 = dFormat.parse(dFormat.format(date));
									
									long timeDifInMillis = date1.getTime() - date2.getTime();
									Date dateDiff = new Date(timeDifInMillis);
									
									Calendar diffCal = Calendar.getInstance();
									diffCal.setTime(dateDiff);
									
									int age = (diffCal.get(Calendar.YEAR) - 1970);
									
									String dp = Double.toString(calculateBloodPressure(data, age));
									
									// use this
									mDataField.setText(dp); // data
									
								} catch (ParseException e)
								{
									e.printStackTrace();
								}
							}
							
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError)
						{
						
						}
					});
		}
	}
	
	
	private void displayGattServices(List<BluetoothGattService> gattServices)
	{
		if (gattServices == null) return;
		String uuid = null;
		String unknownServiceString = getResources().getString(R.string.unknown_service);
		String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
				= new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
		
		
		int i = 0;
		for (BluetoothGattService gattService : gattServices)
		{
			i++;
			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			currentServiceData.put(
					LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
			//edit
			currentServiceData.put(LIST_UUID, uuid);
			
			gattServiceData.add(currentServiceData);
			
			ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
					new ArrayList<HashMap<String, String>>();
			List<BluetoothGattCharacteristic> gattCharacteristics =
					gattService.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas =
					new ArrayList<BluetoothGattCharacteristic>();
			
			
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
			{
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();
				currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
				//edit
				currentCharaData.put(LIST_UUID, uuid);
				
				gattCharacteristicGroupData.add(currentCharaData);
			}
			Log.d("" + i, "" + unknownCharaString);
			mGattCharacteristics.add(charas);
			gattCharacteristicData.add(gattCharacteristicGroupData);
		}
		
		SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
				this,
				gattServiceData,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{LIST_NAME, LIST_UUID},
				new int[]{android.R.id.text1, android.R.id.text2},
				gattCharacteristicData,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{LIST_NAME, LIST_UUID},
				new int[]{android.R.id.text1, android.R.id.text2}
		);
		mGattServicesList.setAdapter(gattServiceAdapter);
	}
	
	private static IntentFilter makeGattUpdateIntentFilter()
	{
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}
	
	@Override
	public void bPMValueReceive(final double value)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(getApplicationContext(), "Your Heart Rate : " + value, Toast.LENGTH_SHORT);
			}
		});
		
	}
	
	/**
	 * Calculate BP
	 *
	 * @param heartRate
	 * @param age
	 * @return
	 */
	private double calculateBloodPressure(final String heartRate, int age)
	{
		
		double sv = 0, svr = 0;
		
		double hr = Double.parseDouble(heartRate);
		
		if (0 > age && age < 2.9)
		{
			sv = 23;
			svr = 2045;
		}
		else if (3 > age && age < 5.9)
		{
			sv = 40.1;
			svr = 1496;
		}
		else if (6 > age && age < 11.9)
		{
			sv = 59;
			svr = 12.82;
		}
		else if (12 > age && age < 17.9)
		{
			sv = 79;
			svr = 1043;
		}
		else if (18 > age && age < 29.9)
		{
			sv = 76.3;
			svr = 1244;
			
		}
		else if (30 > age && age < 59.9)
		{
			sv = 68.8;
			svr = 1566;
			
		}
		else
		{
			sv = 23;
			svr = 1435;
		}
		
		return hr * sv * svr;
		
	}
}

