/*
 * servo.h
 *
 *  Created on: Apr 6, 2022
 *      Author: ohenning
 */
#include <stdint.h>
#include <stdio.h>
#include <stdbool.h>
#include <inc/tm4c123gh6pm.h>
#include "driverlib/interrupt.h"

#ifndef SERVO_H_
#define SERVO_H_

void servo_init(void);

void servo_move(double degrees);

void servo_calibrate(double fact, double diff);


#endif /* SERVO_H_ */
