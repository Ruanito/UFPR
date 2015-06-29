package br.ufpr.inf.rds11.ci306_final;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Maze extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);
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


        while (nao parar){ //botão de parada na tela
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

    boolean canRight(){ //check whether it is possible to turn right
        if (ultrasom direito > 35){
            return true;
        }else{
            return false;
        }
    }


    boolean canForward(){ //check whether it is possible to go forward
        if (ultrasom frontal > 35){
            return true;
        }else{
            return false;
        }
    }

    boolean canLeft(){ //check whether it is possible to turn left
        if (ultrasom esquerdo > 35){
            return true;
        }else{
            return false;
        }
    }

    //routes corrections
    void correctAngle(int &leituraA,int &leituraB,int &aPrev, int &bPrev){
        delay(100);
        int a = ultrasom direito;
        int b = ultrasom esquerdo;
        int dist = ultrasom frontal;
        int c;
        c = a+b;

        if((c < 40) && (a != b)){
            if (a < b){
                while (a < b){
                    rotate_left();
                    delay(30);
                    parar();
                    delay(70);
                    a = ultrasom direito;
                    if (b > 24){
                        rotate_left();
                        delay(50);
                        parar();
                        forward();
                        delay(70);
                        parar();
                        return;
                    }
                    delay(100);
                    b = ultrasom esquerdo;
                }
            }else{
                if (b < a){
                    while (b < a){
                        rotate_right();
                        delay(30);
                        parar();
                        delay(70);
                        b = ultrasom esquerdo;
                        if (a > 24){
                            rotate_right();
                            delay(50);
                            parar();
                            forward();
                            delay(70);
                            parar();
                            return;
                        }
                        delay(100);
                        a = ultrasom direito;
                    }
                }
            }
        }

        else if (c > 40){
            if(lWall()){
                if(leituraB == 0){
                    delay(100);
                    bPrev = ultrasom esquerdo;
                    leituraB = 1;
                }

                if (leituraB == 1){
                    if( b > bPrev){
                        while ( b > bPrev){
                            rotate_left();
                            delay(30);
                            parar();
                            delay(70);
                            b =  ultrasom esquerdo;
                        }
                    }else if (b < bPrev){
                        while (b < bPrev){
                            rotate_right();
                            delay(30);
                            parar();
                            delay(70);
                            b =  ultrasom esquerdo;
                        }
                    }
                }
            }else if (rWall()){
                if(leituraA == 0){
                    delay(100);
                    aPrev = ultrasom direito;
                    leituraA = 1;
                }

                if (leituraA == 1){
                    if ( a > aPrev){
                        while ( a > aPrev){
                            rotate_right();
                            delay(50);
                            parar();
                            delay(70);
                            a =  ultrasom direito;
                        }
                    }else if( a < aPrev){
                        while ( a < aPrev){
                            rotate_left();
                            delay(50);
                            parar();
                            delay(70);
                            a =  ultrasom direito;
                        }
                    }
                }
            }else if (dist > 45){
                forward();
                delay(200);
                parar();
            }
        }
    }

    //displacement functions
    void rotRight(){  //vira 90 graus a direita
        rotate_right();
        delay(400);
        parar();
    }

    void rotLeft(){ //vira 90 graus a esquerda
        rotate_left();
        delay(400);
        parar();
    }

    void rot180degrees(){ //vira 180 graus
        rotate_left();
        delay(800);
        parar();
    }

    void controlForward()
    {
        delay(100);
        int dist = ultrasom frontal;
        int pos;
        int x, xPrev;
        if ((dist < 230) && (dist > 180)){
            xPrev = 1;

            while(dist > 170){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                parar();
                delay(50);
                dist = ultrasom frontal;
            }
        } else if ((dist < 185) && (dist > 121)){
            xPrev = 2;

            while(dist > 116){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                parar();
                delay(50);
                dist = ultrasom frontal;
            }
        } else if ((dist < 121) && (dist > 63)){
            xPrev = 3;

            while(dist > 63){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                parar();
                delay(50);
                dist = ultrasom frontal;
            }
        } else if ((dist < 68) && (dist > 13)){
            xPrev = 4;

            while(dist > 13){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                parar();
                delay(50);
                dist = ultrasom frontal;
            }
        }
        delay(100);
    }

    //wall detector
    boolean LRWall(){ //detect left and right wall
        delay(100);
        if (ultrasom esquerdo < 25){
            delay(100);
            if (ultrasom frontal < 25){
                return true;
            } else {
                return false;
            }
        }
    }

    boolean lWall(){ //detect left wall
        delay(100);
        if (ultrasom esquerdo < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean rWall(){ //detect right wall
        delay(100);
        if (ultrasom direito < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean fWall(){ //detect front wall
        delay(100);
        if (ultrasom frontal < 30){
            return true;
        } else {
            return false;
        }
    }



}
