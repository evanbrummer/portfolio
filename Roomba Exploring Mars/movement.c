/*
 * movement.c
 *
 *  Created on: Feb 2, 2022
 *      Author: ebrummer
 *
 *      UPDATED FOR LAB 7
 */

#include "open_interface.h"
#include "uart.h"

const MAX_CLIFF_SIGNAL = 2650;
int TAPE_SIGNAL_EN = 1;

int DOFdrone = 0, DOFend =  1, starpwr = 2;
int numNotes = 5;
unsigned char notes[] = {55, 57, 55, 54, 52};//g3,a3,g3,f#3,e3
unsigned char durs[] = {12, 12, 6, 6, 12};
unsigned char endNotes[] = {79, 81, 79, 78, 76};//g3,a3,g3,f#3,e3


void move_forward(oi_t *sensor_data, double distance_mm) {
    // the following code could be put in function move_forward()
    putty_printf("[m] Forward\n\r");


        double sum = 0; // distance member in oi_t struct is type double
        char cliffSignal[50];

        oi_setWheels(75, 75); //move forward at full speed

        while (sum < distance_mm)
        {
            oi_update(sensor_data);
            sum += sensor_data->distance; // use -> notation since pointer

            // Cliff SENSOR (fall!) check
            if (sensor_data->cliffFrontLeft || sensor_data->cliffFrontLeftSignal < 1500) {
                putty_printf("[C] FRONT LEFT Cliff Sensor\n\r");
                oi_play_song(DOFend);
                break;
            }
            else if (sensor_data->cliffFrontRight || sensor_data->cliffFrontRightSignal < 1500) {
                putty_printf("[C] FRONT RIGHT Cliff Sensor\n\r");
                oi_play_song(DOFend);
                break;
            }
            else if (sensor_data->cliffLeft || sensor_data->cliffLeftSignal < 1500) {
                putty_printf("[C] FAR LEFT Cliff Sensor\n\r");
                oi_play_song(DOFend);
                break;
            }
            else if (sensor_data->cliffRight || sensor_data->cliffRightSignal < 1500) {
                putty_printf("[C] FAR RIGHT Cliff Sensor\n\r");
                oi_play_song(DOFend);
                break;
            }

            /*
            if (sensor_data->lightBumperCenterLeft) {
                putty_printf("[B] CENTER LEFT Bump sensor\n\r");
                break;
            }
            else if (sensor_data->lightBumperCenterRight) {
                putty_printf("[B] CENTER RIGHT Bump sensor\n\r");
                break;
            }
            else if (sensor_data->lightBumperFrontLeft) {
                putty_printf("[B] FRONT LEFT Bump sensor\n\r");
                break;
            }
            else if (sensor_data->lightBumperFrontRight) {
                putty_printf("[B] FRONT RIGHT Bump sensor\n\r");
                break;
            }
            else if (sensor_data->lightBumperLeft) {
                putty_printf("[B] FAR LEFT Bump sensor\n\r");
                break;
            }
            else if (sensor_data->lightBumperRight) {
                putty_printf("[B] FAR RIGHT Bump sensor\n\r");
                break;
            } */

            // Simple bump sensors
            if (sensor_data->bumpLeft) {
                putty_printf("[B] LEFT Bump sensor");
                oi_play_song(DOFdrone);
                break;
            }
            else if (sensor_data->bumpRight) {
                putty_printf("[B] RIGHT Bump sensor");
                oi_play_song(DOFdrone);
                break;
            }

            // Cliff signal (tape) check
            if (TAPE_SIGNAL_EN == 1) {
                if (sensor_data->cliffFrontLeftSignal > MAX_CLIFF_SIGNAL) {
                    putty_printf("[T]  FRONT LEFT Tape Signal\n\r");
                    break;
                }
                else if (sensor_data->cliffFrontRightSignal > MAX_CLIFF_SIGNAL) {
                    putty_printf("[T]  FRONT RIGHT Tape Signal\n\r");
                    break;
                }
                else if (sensor_data->cliffLeftSignal > MAX_CLIFF_SIGNAL) {
                    putty_printf("[T]  FAR LEFT Tape Signal\n\r");
                    break;
                }
                else if (sensor_data->cliffRightSignal > MAX_CLIFF_SIGNAL) {
                    putty_printf("[T]  FAR RIGHT Tape Signal\n\r");
                    break;
                }
            }


            lcd_printf("%f", sum);

        }

        oi_setWheels(0, 0); //stop

}

void move_backward(oi_t *sensor_data, double distance_mm) {
    putty_printf("[m] Backward\n\r");
    double sum = 0;

    oi_setWheels(-100, -100);

    while (sum < distance_mm) {
        oi_update(sensor_data);
        sum += abs(sensor_data->distance);
        lcd_printf("%f", sum);
    }

    oi_setWheels(0, 0);

}

void turn_left(oi_t *sensor_data, double degrees) {
    char turnStr[50];
        sprintf(turnStr, "[t] LEFT %.1f degrees\n\r", 10.0);
        putty_printf(turnStr);
    double sum = 0;

    oi_setWheels(75, -75);

    while (sum < degrees) {
        oi_update(sensor_data);
        sum += sensor_data->angle;
        lcd_printf("%f", sum);
    }

    oi_setWheels(0, 0);

}

void turn_right(oi_t *sensor_data, double degrees) {
    char turnStr[50];
    sprintf(turnStr, "[t] RIGHT %.1f degrees\n\r", 10.0);
    putty_printf(turnStr);
    double sum = 0;

        oi_setWheels(-75, 75);

        while (sum < degrees) {
            oi_update(sensor_data);
            sum += abs(sensor_data->angle);
            lcd_printf("%f", sum);
        }

        oi_setWheels(0, 0);
}

void toggle_tape_signal() {
    TAPE_SIGNAL_EN *= -1;
    if (TAPE_SIGNAL_EN == 1) {
        putty_printf("[i] Tape signal ENABLED\n\r");
    } else {
        putty_printf("[i] Tape signal DISABLED\n\r");
    }
}
