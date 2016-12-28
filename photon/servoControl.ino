Servo myservo;  // create servo object to control a servo<br>
int pos = 0; 
int servoposition=0;
void setup(){
  myservo.attach(D0);  // attaches the servo on the A0 pin to the servo object
  Spark.function("setpos", setPosition);
  Spark.variable("getpos", &servoposition, INT);
  myservo.write(0);
  servoposition=myservo.read();
}



int setPosition(String posValue) {
    pos = posValue.toInt();
    myservo.write(pos);
    servoposition=myservo.read();
    return servoposition;
}