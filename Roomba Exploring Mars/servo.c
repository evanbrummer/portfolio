/*
 * servo.c
 *
 *  Created on: Apr 6, 2022
 *      Author: ohenning
 */
#include "Timer.h"
#include <stdint.h>
#include <stdio.h>
//#include "driverlib/interrupt.h"
#include "servo.h"
#include "lcd.h"
//#include "open_interface.h"
#include <inc/tm4c123gh6pm.h>

double SERVO_FACT = 1.15;
double SERVO_DIFF = 125;

void servo_init(void){

    SYSCTL_RCGCGPIO_R |= 0x2;
    while((SYSCTL_PRGPIO_R & 0x02) == 0){};
    SYSCTL_RCGCTIMER_R |= 0x2;
    while((SYSCTL_RCGCTIMER_R & 0x02) == 0){};

    GPIO_PORTB_DEN_R |= 0x20;
    GPIO_PORTB_DIR_R |= 0x20;
    GPIO_PORTB_AFSEL_R |= 0x20;
    GPIO_PORTB_PCTL_R |= 0x700000;


    TIMER1_CTL_R &= ~0x0100;
//    TIMER1_CTL_R |= 0x0C00;

    TIMER1_CFG_R |= 0x4;     //USE BAI 9.2 PWM for this shit
    TIMER1_TBMR_R |= 0xA;
    TIMER1_CTL_R &= 0xFFFFBFFF;
    //TIMER1_TBMR_R &= ~0x10;

//    TIMER1_TBR = 0x000;     //end value??
//    TIMER1_ICR_R = 0x400;
//    TIMER1_IMR_R = 0x400;
//    NVIC_EN1_R = 0x10;
//    NVIC_PRI9_R = 0xA0;


  //  TIMER1_CTL_R |= 0x0100;
}

void servo_move(double degrees){
    double realDegrees = (degrees * SERVO_FACT - SERVO_DIFF);
    TIMER1_CTL_R &= ~0x0100;

    TIMER1_TBPR_R = 0x4; //prescaler
    TIMER1_TBILR_R = 0xe200; //start value

    //TIMER1_TBPR_R = 0x000;   //end value
    TIMER1_TBPMR_R = 0x4; //edited
    //TIMER1_TBMATCHR_R = 0xc000; //how servo moves with precision (width of pulse*)

    int cycles = (16000000 * ((realDegrees / 180.0) + 1)) / 1000; //might need to divide by 1000??? some number
    int shiftVal = cycles & 0xffff;
    TIMER1_TBMATCHR_R = (320000 - shiftVal) & 0xFFFF;  //320000 - ms & 0xffff
    TIMER1_TBPMR_R = (320000 - cycles)>>16; //???

    TIMER1_CTL_R |= 0x0100;

    timer_waitMillis(40);
}

void servo_calibrate(double fact, double diff) {
    SERVO_FACT = fact;
    SERVO_DIFF = diff;
}

