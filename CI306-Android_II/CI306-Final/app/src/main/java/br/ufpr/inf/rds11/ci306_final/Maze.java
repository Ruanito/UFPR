package br.ufpr.inf.rds11.ci306_final;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Maze extends ActionBarActivity {

    private BluetoothArduino mBlue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        mBlue = BluetoothArduino.getInstance("linvor");

        mBlue.Connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_labirinto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    int left, front, right;
    int line, column, angle, leituraA = 0, leituraB = 0, aPrev, bPrev;

    void loop()
    {
        line = 1;
        column = 1;
        angle = 0;


        while (nao stopWalking){ //botão de parada na tela
            delay(1000);
            if (canRight()){
                rotRight();
                controlForward();
                newAngle(angle,90);
                newPosition(line,column,angle);
                leituraA = 0; leituraB  = 0;
            }else if(canForward()){
                controlForward();
                newPosition(line,column,angle);
                leituraA = 0; leituraB  = 0;
            }else if(canLeft()){
                rotLeft();
                controlForward();
                newAngle(angle,-90);
                newPosition(line,column,angle);
                leituraA = 0; leituraB  = 0;
            }else{
                rot180degrees();
                controlForward();
                newAngle(angle,180);
                newPosition(line,column,angle);
                leituraA = 0; leituraB  = 0;
            }
            delay(1000);
        }
    }
    //============================================end-loop============================================//
//position functions
    int newAngle(int &angle, int action){ //set angle after movement
        angle = angle + action;
        if (angle >= 360){
            angle = angle - 360;
        }else if(angle < 0){
            angle = angle + 360;
        }
    }

    int newPosition(int &line, int &column, int ang){ //set position xy after movement
        if (ang == 0){
            line += 1;
        }else if(ang == 90){
            column += 1;
        }else if(ang == 180){
            line -= 1;
        }else if(ang == 270){
            column -=1;
        }
    }

    int ultrasomLeft () {
        mBlue.SendMessage("5");
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String left = mBlue.getMenssageSpacial(3);
        return Integer.parseInt(left);
    }

    int ultrasomRight () {
        mBlue.SendMessage("5");
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String right = mBlue.getMenssageSpacial(5);
        return Integer.parseInt(right);
    }

    int ultrasomFront () {
        mBlue.SendMessage("5");
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String front = mBlue.getMenssageSpacial(1);
        return Integer.parseInt(front);
    }

    boolean canRight(){ //check whether it is possible to turn right
        if (ultrasomRight() > 35){
            return true;
        }else{
            return false;
        }
    }

    boolean canForward(){ //check whether it is possible to go forward
        if (ultrasomFront() > 35){
            return true;
        }else{
            return false;
        }
    }

    boolean canLeft(){ //check whether it is possible to turn left
        if (ultrasomLeft() > 35){
            return true;
        }else{
            return false;
        }
    }

    //routes corrections
    void correctAngle(int &leituraA,int &leituraB,int &aPrev, int &bPrev){
        delay(100);
        int a = ultrasomRight() ;
        int b = ultrasomLeft ();
        int dist = ultrasomFront();
        int c;
        c = a+b;

        if((c < 40) && (a != b)){
            if (a < b){
                while (a < b){
                    rotate_left();
                    delay(30);
                    stopWalking();
                    delay(70);
                    a = ultrasom Right ;
                    if (b > 24){
                        rotate_left();
                        delay(50);
                        stopWalking();
                        forward();
                        delay(70);
                        stopWalking();
                        return;
                    }
                    delay(100);
                    b = ultrasomLeft ();
                }
            }else{
                if (b < a){
                    while (b < a){
                        rotate_right();
                        delay(30);
                        stopWalking();
                        delay(70);
                        b = ultrasomLeft ();
                        if (a > 24){
                            rotate_right();
                            delay(50);
                            stopWalking();
                            forward();
                            delay(70);
                            stopWalking();
                            return;
                        }
                        delay(100);
                        a = ultrasom Right ;
                    }
                }
            }
        }

        else if (c > 40){
            if(lWall()){
                if(leituraB == 0){
                    delay(100);
                    bPrev = ultrasomLeft ();
                    leituraB = 1;
                }

                if (leituraB == 1){
                    if( b > bPrev){
                        while ( b > bPrev){
                            rotate_left();
                            delay(30);
                            stopWalking();
                            delay(70);
                            b =  ultrasomLeft ();
                        }
                    }else if (b < bPrev){
                        while (b < bPrev){
                            rotate_right();
                            delay(30);
                            stopWalking();
                            delay(70);
                            b =  ultrasomLeft ();
                        }
                    }
                }
            }else if (rWall()){
                if(leituraA == 0){
                    delay(100);
                    aPrev = ultrasom Right ;
                    leituraA = 1;
                }

                if (leituraA == 1){
                    if ( a > aPrev){
                        while ( a > aPrev){
                            rotate_right();
                            delay(50);
                            stopWalking();
                            delay(70);
                            a =  ultrasom Right ;
                        }
                    }else if( a < aPrev){
                        while ( a < aPrev){
                            rotate_left();
                            delay(50);
                            stopWalking();
                            delay(70);
                            a =  ultrasom Right ;
                        }
                    }
                }
            }else if (dist > 45){
                forward();
                delay(200);
                stopWalking();
            }
        }
    }

    void rotate_right() {
        mBlue.SendMessage("F");
    }

    void stopWalking() {
        mBlue.SendMessage("0");
    }

    void rotate_left() {
        mBlue.SendMessage("E");
    }

    void tornRight(){  //vira 90 graus a direita
        rotate_right();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWalking();
    }

    void turnLeft(){ //vira 90 graus a esquerda
        rotate_left();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWalking();
    }

    void rot180degrees(){ //vira 180 graus
        rotate_left();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWalking();
    }


    void controlForward()
    {
        delay(100);
        int dist = ultrasomFront();
        int pos;
        int x, xPrev;
        if ((dist < 230) && (dist > 180)){
            xPrev = 1;

            while(dist > 170){
                correctAngle(leituraA, leituraB, aPrev,bPrev);
                forward();
                delay(50);
                stopWalking();
                delay(50);
                dist = ultrasomFront();
            }
        } else if ((dist < 185) && (dist > 121)){
            xPrev = 2;

            while(dist > 116){
                correctAngle(leituraA, leituraB, aPrev,bPrev);
                forward();
                delay(50);
                stopWalking();
                delay(50);
                dist = ultrasomFront();
            }
        } else if ((dist < 121) && (dist > 63)){
            xPrev = 3;

            while(dist > 63){
                correctAngle(leituraA, leituraB, aPrev,bPrev);
                forward();
                delay(50);
                stopWalking();
                delay(50);
                dist = ultrasomFront();
            }
        } else if ((dist < 68) && (dist > 13)){
            xPrev = 4;

            while(dist > 13){
                correctAngle(leituraA, leituraB, aPrev,bPrev);
                forward();
                delay(50);
                stopWalking();
                delay(50);
                dist = ultrasomFront();
            }
        }
        delay(100);
    }

    boolean LRWall(){ //detect left and right wall
        if (ultrasomLeft () < 25){
            if (ultrasomFront() < 25){
                return true;
            } else {
                return false;
            }
        }
    }

    boolean lWall(){ //detect left wall
        if (ultrasomLeft () < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean rWall(){ //detect right wall
        if (ultrasomRight()  < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean fWall(){ //detect front wall
        if (ultrasomFront() < 30){
            return true;
        } else {
            return false;
        }
    }



}
