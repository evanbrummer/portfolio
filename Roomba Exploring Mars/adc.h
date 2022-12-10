#include <inc/tm4c123gh6pm.h>
#include <stdint.h>

#ifndef ADC_H_
#define ADC_H_

// initialize and configure the registers needed to sample the analog voltage from the IR distance sensor
void adc_init(void);

// take/read samples from ADC
int16_t adc_read(void);

#endif /* ADC_H_ */
