Implementation of Nagamochi Ibaraki Algorithm

Homework 5: Computing Min Cut in an Undirected Graph

Programming Language: Java

@Authors:
Yash Pradhan: ypp170130

Instructions to compile and execute code:

The folder with name: ypp170130 contains java source files "NagamochiIbaraki.java"

Steps for running code from cmd prompt:

NOTE: while executing from command prompt, the pwd should be the 'src' directory containing the directory ypp170130.

1. Compile
> javac ypp170130/NagamochiIbaraki.java

2. Run
> java ypp170130/NagamochiIbaraki



Expected Output:

Slides Example:
#Vertices: 6; #Edges: 7
------------------------------------
{0,	6,	0,	0,	0,	0,	},
{6,	0,	8,	3,	0,	0,	},
{0,	8,	0,	0,	1,	0,	},
{0,	3,	0,	0,	20,	5,	},
{0,	0,	1,	20,	0,	2,	},
{0,	0,	0,	5,	2,	0,	},
------------------------------------
Min Cut: 4


Random Run
 #edges 50: 0
 #edges 55: 3
 #edges 60: 14
 #edges 65: 27
 #edges 70: 12
 #edges 75: 13
 #edges 80: 20
 #edges 85: 39
 #edges 90: 46
 #edges 95: 41
 #edges 100: 26
 #edges 105: 58
 #edges 110: 56
 #edges 115: 72
 #edges 120: 113
 #edges 125: 86
 #edges 130: 54
 #edges 135: 54
 #edges 140: 73
 #edges 145: 41
 #edges 150: 80
 #edges 155: 111
 #edges 160: 106
 #edges 165: 99
 #edges 170: 77
 #edges 175: 119
 #edges 180: 128
 #edges 185: 130
 #edges 190: 126
 #edges 195: 79
 #edges 200: 122
 #edges 205: 118
 #edges 210: 147
 #edges 215: 146
 #edges 220: 148
 #edges 225: 148
 #edges 230: 192
 #edges 235: 145
 #edges 240: 175
 #edges 245: 198
 #edges 250: 136
 #edges 255: 237
 #edges 260: 168
 #edges 265: 200
 #edges 270: 180
 #edges 275: 237
 #edges 280: 241
 #edges 285: 178
 #edges 290: 249
 #edges 295: 266
 #edges 300: 280
 #edges 305: 238
 #edges 310: 303
 #edges 315: 262
 #edges 320: 201
 #edges 325: 247
 #edges 330: 272
 #edges 335: 287
 #edges 340: 283
 #edges 345: 245
 #edges 350: 197
 #edges 355: 317
 #edges 360: 266
 #edges 365: 310
 #edges 370: 302
 #edges 375: 290
 #edges 380: 350
 #edges 385: 250
 #edges 390: 368
 #edges 395: 315
 #edges 400: 287
 #edges 405: 328
 #edges 410: 379
 #edges 415: 363
 #edges 420: 409
 #edges 425: 406
 #edges 430: 421
 #edges 435: 368
 #edges 440: 387
 #edges 445: 335
 #edges 450: 349
 #edges 455: 423
 #edges 460: 320
 #edges 465: 464
 #edges 470: 407
 #edges 475: 445
 #edges 480: 521
 #edges 485: 363
 #edges 490: 448
 #edges 495: 408
 #edges 500: 474
 #edges 505: 475
 #edges 510: 393
 #edges 515: 432
 #edges 520: 440
 #edges 525: 498
 #edges 530: 535
 #edges 535: 401
 #edges 540: 436
 #edges 545: 470
 #edges 550: 474