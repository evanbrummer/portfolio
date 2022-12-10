#include <inc/tm4c123gh6pm.h>
#include <stdint.h>
#include <stdlib.h>
#include "adc.h"

void adc_init(void) {
    SYSCTL_RCGCADC_R |= 0x01;
    SYSCTL_RCGCGPIO_R |= 0x02;
    while ((SYSCTL_PRGPIO_R & 0x02) == 0) {};
    GPIO_PORTB_DIR_R &= ~0x10;
    GPIO_PORTB_AFSEL_R |= 0x10;
    GPIO_PORTB_DEN_R &= ~0x10;
    GPIO_PORTB_AMSEL_R |= 0x10;
    while ((SYSCTL_PRADC_R&0x0001) != 0x0001) {};
    ADC0_PC_R &= ~0xF;
    ADC0_PC_R |= 0x1;
    ADC0_SSPRI_R = 0x0123;
    ADC0_ACTSS_R &= ~0x0008;
    ADC0_EMUX_R &= ~0xF000;
    ADC0_SSMUX3_R &= ~0x000F;
    ADC0_SSMUX3_R += 10;
    ADC0_SSCTL3_R = 0x0006;
    ADC0_IM_R &= ~0x0008;
    ADC0_ACTSS_R |= 0x0008;
}

int16_t adc_read(void) {
    int16_t result;
    ADC0_PSSI_R = 0x0008;
    while((ADC0_RIS_R&0x08)==0){};
    result = ADC0_SSFIFO3_R&0xFFF;
    ADC0_ISC_R = 0x0008;
    return result;
}
