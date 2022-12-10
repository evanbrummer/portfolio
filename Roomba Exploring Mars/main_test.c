/**
 * lab7_main.c
 *
 * Copy of template file for CprE 288 Lab 6, being used for Lab 7
 *
 * @author Evan Brummer / Cullen White, 3/9/22
 *
 */

#include "Timer.h"
#include "lcd.h"
#include "movement.h"
#include "open_interface.h"
#include "uart.h"
#include "servo.h"
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <math.h>
#include "driverlib/interrupt.h"
#include "adc.h"

// Uncomment or add any include directives that are needed
// #include "button.h"

//STRUCT
typedef struct {
    float sound_dist;
    int IR_raw_val;
} Scan_t;

typedef struct {
        int radWidth;
        float width;
        float distance;
        int edge1Ang;
        int edge2Ang;
} obstacle;

int16_t sample;
Scan_t scan_data;

// #warning "Possible unimplemented functions"
#define REPLACEME 0

// PROTOTYPES
double calcDistance(int16_t x);
void cyBOT_Scan(int angle, Scan_t* didnt_ask);

int main(void) {
	timer_init(); // Must be called before lcd_init(), which uses timer functions
	lcd_init();
	uart_init();
	adc_init();
	servo_init();

	//cyBOT_SERVO_cal();
    //right_calibration_value = 316750;
    //left_calibration_value = 1288000;
	servo_calibrate(1.78, 90); // FACT, DIFF

    oi_t *sensor_data = oi_alloc(); // do this only once at start of main()
    oi_init(sensor_data); // do this only once at start of main()

    char c ='\0';


    int sum = 0;

    int deg;

    // EdITABLE CONSTATNNT
    const IR_MIN = 900;
    const DEG_INC = 2;
    const NUM_OBJECTS = 15;
    double DEG_SCALE = 0.3;
    double MIN_RANGE = 500.0;
    double TURN_FACT_L = 0.75;
    double TURN_FACT_R = 0.75;

    // SOUND VARS
    int DOFdrone = 0, DOFend =  1, starpwr = 2;
    int numNotes = 5;
    unsigned char notes[] = {55, 57, 55, 54, 52};//g3,a3,g3,f#3,e3
    unsigned char durs[] = {12, 12, 6, 6, 12};
    unsigned char endNotes[] = {79, 81, 79, 78, 76};//g3,a3,g3,f#3,e3
    oi_loadSong(DOFdrone, numNotes, notes, durs);
    oi_loadSong(DOFend, numNotes, endNotes, durs);
    //oi_play_song(DOFdrone);
    //oi_play_song(DOFend);



    char str[40];
    int myAngle = 0;
    obstacle items[20];
    int count = 0;
    bool objectStarted = false;
    int i;
    //char c;
    int smolIndex, IRList[3], avgIR;
    float DistList[3], avgDist;


    double currDist;
    double prevDist;

    int currIR;
    int prevIR;
    int degMeasure;
    bool objFlag;

    int degLocation[NUM_OBJECTS];
    int degSize[NUM_OBJECTS];

    double dist[180];
    double objSize[NUM_OBJECTS];
//    double avgDist[NUM_OBJECTS];
    int dist_i;

    int objIndex = 0;
    int clearObj;
    int clearDist;

    bool repeatScan = false;
    bool reachedObject = false;

    int turnTo;
    int k;

    char stats[50];
    char objStats[50];
    char signalStr[50];

    while (true)
    {
        c = uart_receive_nonblocking(); // OUR OWN UART FUNCTION
        if (repeatScan) {
            c = 'o';
        }
        lcd_printf("%c", c);

        switch (c) // S W I T C H   T O   M O V E  A N D   S C A N =============================
        {
        case 'w': // Manual forward
            move_forward(sensor_data, 50);
            //oi_setWheels(150, 150);
            break;

        case 'a': // Manual turn left
            turn_left(sensor_data, 10 * TURN_FACT_L);
            break;

        case 'd': // Manual turn right
            turn_right(sensor_data, 10 * TURN_FACT_R);
            break;

        case 'x': // Manual backward
            move_backward(sensor_data, 50);
            break;

        case 's': //lol
            oi_play_song(DOFdrone);
            break;
        case 'q': //lol
            oi_play_song(DOFend);
            break;

        case 'm': // Manual scan
            sprintf(str, "\n\rDegrees\tPING Distance\t(cm)\tIR Value \n\r");
            putty_printf(str);
            for(myAngle = 0; myAngle <= 180; myAngle += DEG_INC){

                for(i = 0 ; i < 2 ; i++){
                    cyBOT_Scan(myAngle, &scan_data);
                    DistList[i] = scan_data.sound_dist;
                    IRList[i] = scan_data.IR_raw_val;
                    timer_waitMillis(10);
                }

//                cyBOT_Scan(myAngle, &scan_data);
//                avgDist = scan_data.sound_dist;
//                avgIR = scan_data.IR_raw_val;
                avgDist = (DistList[0] + DistList[1]) / 2.0;
                avgIR = (IRList[0] + IRList[1]) / 2;

                //sprintf(str, "%d\t%d\t%d\n\r", IRList[0], IRList[1], IRList[2]);
                //putty_printf(str);

                sprintf(str, "%d\t%f\t\t%d\n\r", myAngle, avgDist, avgIR);
                putty_printf(str);

                if (avgIR > 780 && !objectStarted && myAngle > 2 && avgDist < 75){    //object detected, start scan
                    objectStarted = true;
                    //items[count].distance = avgDist;
                    items[count].edge1Ang = myAngle;
                }
                if (avgIR < 650 && objectStarted){                                                //object end, stop scan
                    objectStarted = false;
                    items[count].edge2Ang = myAngle;
                    items[count].radWidth = items[count].edge2Ang - items[count].edge1Ang;

                    count++;
                    lcd_printf("Object #: %d", count);
                }
            }                                                                                                    //end object scan


            if (count > 0){                                                                                      //find object midpoints
                for(i = 0; i < count; i++){
                    cyBOT_Scan((items[i].edge1Ang + items[i].edge2Ang)/2, &scan_data);                 //move
                    timer_waitMillis(500);
                    cyBOT_Scan((items[i].edge1Ang + items[i].edge2Ang)/2, &scan_data);                 //scan midpoint



                    items[i].distance = scan_data.sound_dist;                                      //add midpoint dist to struct
                    items[i].width = 2 * items[i].distance * tan((items[i].radWidth * (3.14 / 180)) / 2.0);   //real width (cm)

                    sprintf(str, "dist:%.2f width:%.2f @ %d degrees\n\r", items[i].distance, items[i].width,
                                items[i].edge1Ang + (items[i].edge2Ang - items[i].edge1Ang)/2);
                    putty_printf(str);
                }
            }

            for (i = 0; i < count; i++){
                items[i].radWidth = '\0';
                items[i].width = '\0';
                items[i].distance ='\0';
                items[i].edge1Ang = '\0';
                items[i].edge2Ang = '\0';
            }
            count = 0;


            /* OLD ASH
            objIndex = 0;

            for (clearObj = 0; clearObj < NUM_OBJECTS; clearObj++) {
                degLocation[clearObj] = NULL;
                degSize[clearObj] = NULL;
                objSize[clearObj] = NULL;
                avgDist[clearObj] = NULL;
            }

            for (clearDist = 0; clearDist < 180; clearDist++) {
                dist[clearDist] = NULL;
            }


            putty_printf("\n\r[M] Degrees\t PING Distance (cm)\t IR Value\n\r    ");

            degMeasure = 0;


            for (k = 0; k < NUM_OBJECTS; k++)
            {
                degLocation[k] = 9999;
                degSize[k] = 9999;
            }

            for (deg = 0; deg <= 180; deg = (deg + DEG_INC))
            { // EVERY X DEGREES (0-180)...
                c = uart_receive_nonblocking();
                if (c == 't') {
                    break;
                }

                sample = adc_read();
                cyBOT_Scan(deg, &scan_data);
                sprintf(stats, "%d\t\t %.2f      \t\t %d\n\r", deg,
                        calcDistance(sample), sample); // SAVE STATS TO STRING

                if (deg != 0)
                { // AFTER FIRST ITERATION OF THE LOOP (AFTER DEGREE 0)
                    prevDist = currDist;
                    currDist = calcDistance(sample);
                    dist[deg] = currDist;
                    dist[deg + DEG_INC/2] = currDist;

                    prevIR = currIR;
                    if (deg >= 180) {
                        currIR = 0;
                    } else {
                        currIR = scan_data.IR_raw_val;
                    }

                    if (currIR > IR_MIN)
                    { // IF object is detected
                        putty_printf("[o] "); // print that it's detected
                        degMeasure += DEG_INC; // increment the measurement of the object
                        objFlag = 1; // turn on the object flag

                        degLocation[objIndex] = deg; // store the first degree location of the object in the array

                    }
                    else
                    {
                        putty_printf("    ");
                        objFlag = 0; // turn off the object flag

                        if (degMeasure > DEG_INC && objIndex <= NUM_OBJECTS) // if the size is greater than X and we haven't gone over the array size limit
                        { // add object to arrays
                            degSize[objIndex] = degMeasure;
                            degLocation[objIndex] = deg;

                            for (dist_i = deg;
                                    dist_i > deg - degMeasure; dist_i--)
                            {
                                avgDist[objIndex] += (1.0*dist[dist_i]);
                            }
                            avgDist[objIndex] /= (1.0 * degMeasure);

                            objSize[objIndex] = 0;
                            objSize[objIndex] = degMeasure * dist[abs(degLocation[objIndex] - degSize[objIndex]/2)];

                            sprintf(objStats, "OBJECT %d @ %d degrees, AVG DISTANCE: %.2f, ARC LENGTHH: %.2f\n\r    ",
                                              objIndex, abs(degLocation[objIndex] - degSize[objIndex]/2), (avgDist[objIndex]), objSize[objIndex] ); // save the stats to objStats

                            putty_printf(objStats);

                            objIndex++;

                        }

                        degMeasure = 0;

                    }
                }

                else
                { // AT DEGREE 0, ONLY GET CURRDIST
                    currDist = calcDistance(sample);
                    currIR = sample;
                }

                putty_printf(stats);
            }*/

            break;

        case 'c':
            oi_update(sensor_data);
            sprintf(signalStr,
                    "L: %d / FL: %d / FR: %d / R: %d\n\r",
                    sensor_data->cliffLeftSignal, sensor_data->cliffFrontLeftSignal,
                    sensor_data->cliffFrontRightSignal, sensor_data->cliffRightSignal);
            putty_printf(signalStr);
            break;
        case 'v':
            toggle_tape_signal();
            break;
        case 't':
            servo_move(0);
            break;
        case 'y':
            servo_move(90);
            break;
        case 'u':
            servo_move(180);
            break;
        case 'p':
            lcd_printf("EARTH DIE");
            timer_waitMillis(2000);
            break;
        default:
            oi_setWheels(0, 0);
            break;
        }
    }

}

double calcDistance(int16_t x) {
    return 0.5 * ( 3.1722878 + (113819900 - 3.172878)/(1 + pow(x/0.7406373, 2.019965)) );
}

void cyBOT_Scan(int angle, Scan_t* didnt_ask) {
    servo_move(angle);

    sample = adc_read();

    scan_data.IR_raw_val = sample;
    scan_data.sound_dist = calcDistance(sample);
}
