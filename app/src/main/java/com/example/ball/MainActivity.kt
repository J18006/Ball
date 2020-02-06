package com.example.ball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.*
//import androidx.appcompat.app.APPCompatActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), SensorEventListener, SurfaceHolder.Callback{

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        mSurfaceWidth=width
        mSurfaceHeight=height
        mBallx= (width/2.0).toFloat()
        mBally= (height/2.0).toFloat()
        mVX= 0F
        mVY= 0F
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mSensorManager.unregisterListener(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mSensorManager.registerListener(this,mAccSensor,SensorManager.SENSOR_DELAY_GAME)

    }

    private var mSensorManager:SensorManager by Delegates.notNull<SensorManager>()
    private var mAccSensor:Sensor by Delegates.notNull<Sensor>()

    //フィールドに頑張って追加した変数
    private var mHolder:SurfaceHolder by Delegates.notNull<SurfaceHolder>()
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0
    private var mBallx=0.0f
    private var mBally=0.0f
    private var mVX=0.0f
    private var mVY=0.0f
    private var mFrom=0.toLong()
    private var mTo=0.toLong()

    var flg:Boolean=false
    var left:Int=10
    var top:Int=100
    var right:Int=300
    var bottom:Int=200

    companion object {
        const val RADIUS = 50.0
        const val  COEF = 1000.0
    }
            private fun drawCanvas() {
                var c: Canvas=mHolder.lockCanvas()
                c.drawColor(Color.YELLOW)
                var paint =  Paint()
                paint.setColor(Color.MAGENTA)
                c.drawCircle(mBallx.toFloat(),mBally.toFloat(),RADIUS.toFloat(),paint)
                paint.setColor(Color.BLUE)
                var rect=Rect(left,top,right,bottom)
                c.drawRect(rect,paint)
                if (flg==true){
                    paint.setColor(Color.RED)
                    c.drawRect(rect,paint)
                }

                mHolder.unlockCanvasAndPost(c)


            }



    override fun onCreate(savedinstanceState: Bundle?) {
        super.onCreate(savedinstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mHolder=surfaceView.holder
        mHolder.addCallback(this)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


    override fun onSensorChanged(event: SensorEvent?){
        Log.d("Sensormanager","----------")
        Log.d("x",event?.values!![0].toString())
        Log.d("y",event?.values!![1].toString())
        Log.d("z",event?.values!![2].toString())

        var x=-event?.values!![0]
        var y=event?.values!![1]
        var z=event?.values!![2]

        mTo= System.currentTimeMillis()
        var t=(mTo-mFrom).toFloat()
        t= (t/1000.0f)

        var dx=mVX*t+x*t*t/2.0f
        var dy=mVY*t+y*t*t/2.0f

        mBallx = (mBallx + dx * COEF).toFloat()
        mBally = (mBally + dy * COEF).toFloat()
        mVX = mVX + x * t
        mVY = mVY + y * t

        if (mBallx-RADIUS<0&&mVX<0){
            mVX= (-mVX/1.5f)
            mBallx= RADIUS.toFloat()

        }else if (mBallx+RADIUS>mSurfaceWidth && mVX>0){
            mVX= (-mVX/1.5f)
            mBallx= (mSurfaceWidth-RADIUS).toFloat()

        }
        if (mBally-RADIUS<0&&mVY<0){
            mVY= (-mVY/1.5f)
            mBally= RADIUS.toFloat()

        }
        else if (mBally+RADIUS>mSurfaceHeight && mVY>0){
            mVY= (-mVY/1.5f)
            mBally= (mSurfaceHeight-RADIUS).toFloat()
        }

        mFrom= System.currentTimeMillis().toLong()
        drawCanvas()


        if (left <= mBallx + RADIUS && right >= mBallx - RADIUS
            && top <= mBally + RADIUS && bottom >= mBally - RADIUS){
            flg=true
        }else{
            flg=false
        }


    }








}
