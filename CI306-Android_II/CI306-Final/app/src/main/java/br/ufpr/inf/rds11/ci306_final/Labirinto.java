package br.ufpr.inf.rds11.ci306_final;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Labirinto extends ActionBarActivity {

    private int posSeerv = 100;
    //Servo mysqervo

    //MOTOR
    private int ENA = 7; //black
    private int IN1 = 6; //white
    private int IN2 = 5; //gray

    private int IN3 = 4; //purple
    private int IN4 = 3; //blue
    private int ENB = 2; //green

    //ldr
    //private int LDR = A1;
    //beep
    private int BEEP = 45;

    /*
    private Ultrasonic ultrasonicLeft(31,20);
    private Ultrasonic ultrasonicFront(33,32);
    private
     */
    private int left, front, right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labirinto);
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

    #include <Ultrasonic.h>
    #include <SoftwareSerial.h>
    #include <LiquidCrystal.h>
    #include <Servo.h>
    int posServ = 180;
    Servo myservo;
//motor
    #define ENA 7 // black
            #define IN1 6  // white
            #define IN2 5  // gray

            #define IN3 4  // purple
            #define IN4 3  // blue
            #define ENB 2 // green

//ldr
            #define LDR A1
//beep
    #define BEEP 45

    //ultrasom
    Ultrasonic ultrasonicLeft(31,30);
    Ultrasonic ultrasonicFront(33,32);
    Ultrasonic ultrasonicRight(35,34);
    int left, front, right;
    //LiquidCrystal(rs, rw, enable, d4, d5, d6, d7)
    LiquidCrystal LCD(26, 27, 28, 22, 23, 24, 25);

    //robot position
    int line, column, angle, leituraA = 0, leituraB = 0, aPrev, bPrev;

    void setup()
    {
        pinMode (ENA, OUTPUT);
        pinMode (IN1, OUTPUT);
        pinMode (IN2, OUTPUT);
        pinMode (ENB, OUTPUT);
        pinMode (IN3, OUTPUT);
        pinMode (IN4, OUTPUT);
        pinMode (BEEP, OUTPUT);
        myservo.attach(36);
        Serial.begin(9600);
        Serial3.begin(9600);
    }

    //===============================================loop==============================================//
    void loop()
    {
        while (light()){}

        myservo.write(posServ);
        line = 1;
        column = 1;
        angle = 0;

        String l = String(line);
        String c = String(column);
        String ang = String(angle);
        Serial3.println("--------------------------------------------------------");
        String text = String("|              my start position [" + l + "," + c + "," + ang + "º]              |");
        Serial3.println(text);
        Serial3.println("--------------------------------------------------------");

        while (!light()){
            delay(1000);
            if (canRight()){

                Serial3.println("-----------------------turn right------------------------");
                String l = String(line);
                String c = String(column);
                String ang = String(angle);
                text = String("my position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                rotRight();
                controlForward();
                newAngle(angle,90);
                newPosition(line,column,angle);
                l = String(line);
                c = String(column);
                ang = String(angle);
                String text = String("my new position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                Serial3.println("--------------------------------------------------------");
                leituraA = 0; leituraB  = 0;
            }else if(canForward()){

                Serial3.println("------------------------forward-------------------------");
                String l = String(line);
                String c = String(column);
                String ang = String(angle);
                text = String("my position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                controlForward();
                newPosition(line,column,angle);
                l = String(line);
                c = String(column);
                ang = String(angle);
                text = String("my new position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                Serial3.println("--------------------------------------------------------");
                delay(1000);
                leituraA = 0; leituraB  = 0;
            }else if(canLeft()){

                Serial3.println("-----------------------turn left------------------------");
                String l = String(line);
                String c = String(column);
                String ang = String(angle);
                text = String("my position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                rotLeft(); delay(100);
                controlForward();
                newAngle(angle,-90);
                newPosition(line,column,angle);
                l = String(line);
                c = String(column);
                ang = String(angle);
                text = String("my new position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                Serial3.println("--------------------------------------------------------");
                leituraA = 0; leituraB  = 0;
            }else{
                Serial3.println("-----------------------turn 180º------------------------");
                String l = String(line);
                String c = String(column);
                String ang = String(angle);
                text = String("my position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                rot180degrees();
                controlForward();
                newAngle(angle,180);
                newPosition(line,column,angle);
                l = String(line);
                c = String(column);
                ang = String(angle);
                text = String("my new position [" + l + "," + c + "," + ang + "º]");
                Serial3.println(text);
                Serial3.println("--------------------------------------------------------");
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

    //labirint functions
    boolean light(){  //check for light
        int value = analogRead(LDR);
        LCD.begin(16,2);
        LCD.clear();
        LCD.setCursor(0,0);
        LCD.print(value);
        if (value > 400){
            int posi = 90;
            myservo.write(posi);
            beep_start();
            delay(100);
            beep_stop();
            return true;
        }else{
            return false;
        }
    }

    boolean canRight(){ //check whether it is possible to turn right
        if (ultrasonicRight.Ranging(CM) > 35){
            return true;
        }else{
            return false;
        }
        delay(100);
    }


    boolean canForward(){ //check whether it is possible to go forward
        if (ultrasonicFront.Ranging(CM) > 35){
            return true;
        }else{
            return false;
        }
        delay(100);
    }

    boolean canLeft(){ //check whether it is possible to turn left
        if (ultrasonicLeft.Ranging(CM) > 35){
            return true;
        }else{
            return false;
        }
        delay(100);
    }

    //routes corrections
    void correctAngle(int &leituraA,int &leituraB,int &aPrev, int &bPrev){
        delay(100);
        int a = ultrasonicRight.Ranging(CM);
        delay(100);
        int b = ultrasonicLeft.Ranging(CM);
        delay(100);
        int dist = ultrasonicFront.Ranging(CM);
        int c;
        c = a+b;

        if((c < 40) && (a != b)){
            if (a < b){
                while (a < b){
                    rotate_left();
                    delay(30);
                    stope();
                    delay(70);
                    a = ultrasonicRight.Ranging(CM);
                    if (b > 24){
                        rotate_left();
                        delay(50);
                        stope();
                        forward();
                        delay(70);
                        stope();
                        return;
                    }
                    delay(100);
                    b = ultrasonicLeft.Ranging(CM);
                }
            }else{
                if (b < a){
                    while (b < a){
                        rotate_right();
                        delay(30);
                        stope();
                        delay(70);
                        b = ultrasonicLeft.Ranging(CM);
                        if (a > 24){
                            rotate_right();
                            delay(50);
                            stope();
                            forward();
                            delay(70);
                            stope();
                            return;
                        }
                        delay(100);
                        a = ultrasonicRight.Ranging(CM);
                    }
                }
            }
        }

        else if (c > 40){
            if(lWall()){
                if(leituraB == 0){
                    delay(100);
                    bPrev = ultrasonicLeft.Ranging(CM);
                    leituraB = 1;
                }

                if (leituraB == 1){
                    if( b > bPrev){
                        while ( b > bPrev){
                            rotate_left();
                            delay(30);
                            stope();
                            delay(70);
                            b =  ultrasonicLeft.Ranging(CM);
                        }
                    }else if (b < bPrev){
                        while (b < bPrev){
                            rotate_right();
                            delay(30);
                            stope();
                            delay(70);
                            b =  ultrasonicLeft.Ranging(CM);
                        }
                    }
                }
            }else if (rWall()){
                if(leituraA == 0){
                    delay(100);
                    aPrev = ultrasonicRight.Ranging(CM);
                    leituraA = 1;
                }

                if (leituraA == 1){
                    if ( a > aPrev){
                        while ( a > aPrev){
                            rotate_right();
                            delay(50);
                            stope();
                            delay(70);
                            a =  ultrasonicRight.Ranging(CM);
                        }
                    }else if( a < aPrev){
                        while ( a < aPrev){
                            rotate_left();
                            delay(50);
                            stope();
                            delay(70);
                            a =  ultrasonicRight.Ranging(CM);
                        }
                    }
                }
            }else if (dist > 45){
                forward();
                delay(200);
                stope();
            }
        }
    }

    //displacement functions
    void rotRight(){
        rotate_right();
        delay(400);
        stope();
    }

    void rotLeft(){
        rotate_left();
        delay(400);
        stope();
    }

    void rot180degrees(){
        rotate_left();
        delay(800);
        stope();
    }

    void controlForward()
    {
        delay(100);
        int dist = ultrasonicFront.Ranging(CM);
        int pos;
        int x, xPrev;
        if ((dist < 230) && (dist > 180)){
            xPrev = 1;
            Serial3.println("4 quadrantes a frente ------------");
            while(dist > 170){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                stope();
                delay(50);
                dist = ultrasonicFront.Ranging(CM);
            }
        } else if ((dist < 185) && (dist > 121)){
            xPrev = 2;
            Serial3.println("3 quadrantes a frente ------------");
            while(dist > 116){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                stope();
                delay(50);
                dist = ultrasonicFront.Ranging(CM);
            }
        } else if ((dist < 121) && (dist > 63)){
            xPrev = 3;
            Serial3.println("2 quadrantes a frente ------------");
            while(dist > 63){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                stope();
                delay(50);
                dist = ultrasonicFront.Ranging(CM);
            }
        } else if ((dist < 68) && (dist > 13)){
            xPrev = 4;
            Serial3.println("1 quadrante a frente ------------");
            while(dist > 13){
                correctAngle(leituraA,leituraB,aPrev,bPrev);
                forward();
                delay(50);
                stope();
                delay(50);
                dist = ultrasonicFront.Ranging(CM);
            }
        }
        delay(100);
    }

    //wall detector
    boolean LRWall(){ //detect left and right wall
        delay(100);
        if (ultrasonicLeft.Ranging(CM) < 25){
            delay(100);
            if (ultrasonicFront.Ranging(CM) < 25){
                return true;
            } else {
                return false;
            }
        }
    }

    boolean lWall(){ //detect left wall
        delay(100);
        if (ultrasonicLeft.Ranging(CM) < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean rWall(){ //detect right wall
        delay(100);
        if (ultrasonicRight.Ranging(CM) < 30){
            return true;
        } else {
            return false;
        }
    }

    boolean fWall(){ //detect front wall
        delay(100);
        if (ultrasonicFront.Ranging(CM) < 30){
            return true;
        } else {
            return false;
        }
    }

    //serial functions
    void get_bluetooth(char &blue)
    {
        if (Serial3.available() >0){
            blue = Serial3.read();
        }
    }

    //motor functions
    void forward(){
        digitalWrite (IN1, LOW);
        digitalWrite (IN2, HIGH);
        digitalWrite (IN3, HIGH);
        digitalWrite (IN4, LOW);
        analogWrite (ENA, 200);
        analogWrite (ENB, 255);
    }

    void reverse()
    {
        digitalWrite (IN1, HIGH);
        digitalWrite (IN2, LOW);
        digitalWrite (IN3, LOW);
        digitalWrite (IN4, HIGH);
        analogWrite (ENA, 255);
        analogWrite (ENB, 255);
    }

    void rotate_right()
    {
        digitalWrite (IN1, HIGH);
        digitalWrite (IN2, LOW);
        digitalWrite (IN3, HIGH);
        digitalWrite (IN4, LOW);
        analogWrite (ENA, 220);
        analogWrite (ENB, 220);
    }

    void rotate_left()
    {
        digitalWrite (IN1, LOW);
        digitalWrite (IN2, HIGH);
        digitalWrite (IN3, LOW);
        digitalWrite (IN4, HIGH);
        analogWrite (ENA, 220);
        analogWrite (ENB, 220);
    }

    void stope()
    {
        digitalWrite (IN1, LOW);
        digitalWrite (IN2, LOW);
        digitalWrite (IN3, LOW);
        digitalWrite (IN4, LOW);
        digitalWrite (ENA, LOW);
        digitalWrite (ENB, LOW);
        beep_stop();
    }

    void beep_start ()
    {
        digitalWrite (BEEP, HIGH);
    }

    void beep_stop ()
    {
        digitalWrite (BEEP, LOW);
    }


}
