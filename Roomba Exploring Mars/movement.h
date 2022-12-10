/*
 * movement.h
 *
 *  Created on: Feb 2, 2022
 *      Author: ebrummer
 */

#ifndef MOVEMENT_H_
#define MOVEMENT_H_

#include "open_interface.h"

void move_forward(oi_t *sensor_data, double distance_mm);
void move_backward(oi_t *sensor_data, double distance_mm);
void turn_right(oi_t *sensor_data, double degrees);
void turn_left(oi_t *sensor_data, double degrees);


#endif /* MOVEMENT_H_ */
