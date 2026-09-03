// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.7 - 1.12
// Cleaned up: fixed invalid identifiers (3half1 -> half1, 3half2 -> half2),
// fixed class/constructor name, added package + imports + accessors.
package com.voltyx.mwccf.render.lycoris;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLycoris extends ModelBase {
	private FlowerPart stem;
	private FlowerPart group;
	private FlowerPart cube_r1;
	private FlowerPart group2;
	private FlowerPart cube_r2;
	private FlowerPart group3;
	private FlowerPart cube_r3;
	private FlowerPart group4;
	private FlowerPart cube_r4;
	private FlowerPart group5;
	private FlowerPart cube_r5;
	private FlowerPart group6;
	private FlowerPart cube_r6;
	private FlowerPart group7;
	private FlowerPart cube_r7;
	private FlowerPart flower;
	private FlowerPart half1;
	private FlowerPart perianth4;
	private FlowerPart tepal19;
	private FlowerPart cube_r8;
	private FlowerPart cube_r9;
	private FlowerPart cube_r10;
	private FlowerPart cube_r11;
	private FlowerPart cube_r12;
	private FlowerPart cube_r13;
	private FlowerPart cube_r14;
	private FlowerPart cube_r15;
	private FlowerPart cube_r16;
	private FlowerPart cube_r17;
	private FlowerPart cube_r18;
	private FlowerPart cube_r19;
	private FlowerPart cube_r20;
	private FlowerPart cube_r21;
	private FlowerPart cube_r22;
	private FlowerPart cube_r23;
	private FlowerPart cube_r24;
	private FlowerPart cube_r25;
	private FlowerPart cube_r26;
	private FlowerPart cube_r27;
	private FlowerPart cube_r28;
	private FlowerPart cube_r29;
	private FlowerPart cube_r30;
	private FlowerPart tepal20;
	private FlowerPart cube_r31;
	private FlowerPart cube_r32;
	private FlowerPart cube_r33;
	private FlowerPart cube_r34;
	private FlowerPart cube_r35;
	private FlowerPart cube_r36;
	private FlowerPart cube_r37;
	private FlowerPart cube_r38;
	private FlowerPart cube_r39;
	private FlowerPart cube_r40;
	private FlowerPart cube_r41;
	private FlowerPart cube_r42;
	private FlowerPart cube_r43;
	private FlowerPart cube_r44;
	private FlowerPart cube_r45;
	private FlowerPart cube_r46;
	private FlowerPart cube_r47;
	private FlowerPart cube_r48;
	private FlowerPart cube_r49;
	private FlowerPart cube_r50;
	private FlowerPart cube_r51;
	private FlowerPart cube_r52;
	private FlowerPart cube_r53;
	private FlowerPart tepal21;
	private FlowerPart cube_r54;
	private FlowerPart cube_r55;
	private FlowerPart cube_r56;
	private FlowerPart cube_r57;
	private FlowerPart cube_r58;
	private FlowerPart cube_r59;
	private FlowerPart cube_r60;
	private FlowerPart cube_r61;
	private FlowerPart cube_r62;
	private FlowerPart cube_r63;
	private FlowerPart cube_r64;
	private FlowerPart cube_r65;
	private FlowerPart cube_r66;
	private FlowerPart cube_r67;
	private FlowerPart cube_r68;
	private FlowerPart cube_r69;
	private FlowerPart cube_r70;
	private FlowerPart cube_r71;
	private FlowerPart cube_r72;
	private FlowerPart cube_r73;
	private FlowerPart cube_r74;
	private FlowerPart cube_r75;
	private FlowerPart cube_r76;
	private FlowerPart tepal22;
	private FlowerPart cube_r77;
	private FlowerPart cube_r78;
	private FlowerPart cube_r79;
	private FlowerPart cube_r80;
	private FlowerPart cube_r81;
	private FlowerPart cube_r82;
	private FlowerPart cube_r83;
	private FlowerPart cube_r84;
	private FlowerPart cube_r85;
	private FlowerPart cube_r86;
	private FlowerPart cube_r87;
	private FlowerPart cube_r88;
	private FlowerPart cube_r89;
	private FlowerPart cube_r90;
	private FlowerPart cube_r91;
	private FlowerPart cube_r92;
	private FlowerPart cube_r93;
	private FlowerPart cube_r94;
	private FlowerPart cube_r95;
	private FlowerPart cube_r96;
	private FlowerPart cube_r97;
	private FlowerPart cube_r98;
	private FlowerPart cube_r99;
	private FlowerPart tepal23;
	private FlowerPart cube_r100;
	private FlowerPart cube_r101;
	private FlowerPart cube_r102;
	private FlowerPart cube_r103;
	private FlowerPart cube_r104;
	private FlowerPart cube_r105;
	private FlowerPart cube_r106;
	private FlowerPart cube_r107;
	private FlowerPart cube_r108;
	private FlowerPart cube_r109;
	private FlowerPart cube_r110;
	private FlowerPart cube_r111;
	private FlowerPart cube_r112;
	private FlowerPart cube_r113;
	private FlowerPart cube_r114;
	private FlowerPart cube_r115;
	private FlowerPart cube_r116;
	private FlowerPart cube_r117;
	private FlowerPart cube_r118;
	private FlowerPart cube_r119;
	private FlowerPart cube_r120;
	private FlowerPart cube_r121;
	private FlowerPart cube_r122;
	private FlowerPart tepal24;
	private FlowerPart cube_r123;
	private FlowerPart cube_r124;
	private FlowerPart cube_r125;
	private FlowerPart cube_r126;
	private FlowerPart cube_r127;
	private FlowerPart cube_r128;
	private FlowerPart cube_r129;
	private FlowerPart cube_r130;
	private FlowerPart cube_r131;
	private FlowerPart cube_r132;
	private FlowerPart cube_r133;
	private FlowerPart cube_r134;
	private FlowerPart cube_r135;
	private FlowerPart cube_r136;
	private FlowerPart cube_r137;
	private FlowerPart cube_r138;
	private FlowerPart cube_r139;
	private FlowerPart cube_r140;
	private FlowerPart cube_r141;
	private FlowerPart cube_r142;
	private FlowerPart cube_r143;
	private FlowerPart cube_r144;
	private FlowerPart cube_r145;
	private FlowerPart stemal19;
	private FlowerPart cube_r146;
	private FlowerPart cube_r147;
	private FlowerPart cube_r148;
	private FlowerPart cube_r149;
	private FlowerPart cube_r150;
	private FlowerPart stemal20;
	private FlowerPart cube_r151;
	private FlowerPart cube_r152;
	private FlowerPart cube_r153;
	private FlowerPart cube_r154;
	private FlowerPart cube_r155;
	private FlowerPart stemal21;
	private FlowerPart cube_r156;
	private FlowerPart cube_r157;
	private FlowerPart cube_r158;
	private FlowerPart cube_r159;
	private FlowerPart cube_r160;
	private FlowerPart stemal22;
	private FlowerPart cube_r161;
	private FlowerPart cube_r162;
	private FlowerPart cube_r163;
	private FlowerPart cube_r164;
	private FlowerPart cube_r165;
	private FlowerPart stemal23;
	private FlowerPart cube_r166;
	private FlowerPart cube_r167;
	private FlowerPart cube_r168;
	private FlowerPart cube_r169;
	private FlowerPart cube_r170;
	private FlowerPart stemal24;
	private FlowerPart cube_r171;
	private FlowerPart cube_r172;
	private FlowerPart cube_r173;
	private FlowerPart cube_r174;
	private FlowerPart cube_r175;
	private FlowerPart perianth5;
	private FlowerPart tepal25;
	private FlowerPart cube_r176;
	private FlowerPart cube_r177;
	private FlowerPart cube_r178;
	private FlowerPart cube_r179;
	private FlowerPart cube_r180;
	private FlowerPart cube_r181;
	private FlowerPart cube_r182;
	private FlowerPart cube_r183;
	private FlowerPart cube_r184;
	private FlowerPart cube_r185;
	private FlowerPart cube_r186;
	private FlowerPart cube_r187;
	private FlowerPart cube_r188;
	private FlowerPart cube_r189;
	private FlowerPart cube_r190;
	private FlowerPart cube_r191;
	private FlowerPart cube_r192;
	private FlowerPart cube_r193;
	private FlowerPart cube_r194;
	private FlowerPart cube_r195;
	private FlowerPart cube_r196;
	private FlowerPart cube_r197;
	private FlowerPart cube_r198;
	private FlowerPart tepal26;
	private FlowerPart cube_r199;
	private FlowerPart cube_r200;
	private FlowerPart cube_r201;
	private FlowerPart cube_r202;
	private FlowerPart cube_r203;
	private FlowerPart cube_r204;
	private FlowerPart cube_r205;
	private FlowerPart cube_r206;
	private FlowerPart cube_r207;
	private FlowerPart cube_r208;
	private FlowerPart cube_r209;
	private FlowerPart cube_r210;
	private FlowerPart cube_r211;
	private FlowerPart cube_r212;
	private FlowerPart cube_r213;
	private FlowerPart cube_r214;
	private FlowerPart cube_r215;
	private FlowerPart cube_r216;
	private FlowerPart cube_r217;
	private FlowerPart cube_r218;
	private FlowerPart cube_r219;
	private FlowerPart cube_r220;
	private FlowerPart cube_r221;
	private FlowerPart tepal27;
	private FlowerPart cube_r222;
	private FlowerPart cube_r223;
	private FlowerPart cube_r224;
	private FlowerPart cube_r225;
	private FlowerPart cube_r226;
	private FlowerPart cube_r227;
	private FlowerPart cube_r228;
	private FlowerPart cube_r229;
	private FlowerPart cube_r230;
	private FlowerPart cube_r231;
	private FlowerPart cube_r232;
	private FlowerPart cube_r233;
	private FlowerPart cube_r234;
	private FlowerPart cube_r235;
	private FlowerPart cube_r236;
	private FlowerPart cube_r237;
	private FlowerPart cube_r238;
	private FlowerPart cube_r239;
	private FlowerPart cube_r240;
	private FlowerPart cube_r241;
	private FlowerPart cube_r242;
	private FlowerPart cube_r243;
	private FlowerPart cube_r244;
	private FlowerPart tepal28;
	private FlowerPart cube_r245;
	private FlowerPart cube_r246;
	private FlowerPart cube_r247;
	private FlowerPart cube_r248;
	private FlowerPart cube_r249;
	private FlowerPart cube_r250;
	private FlowerPart cube_r251;
	private FlowerPart cube_r252;
	private FlowerPart cube_r253;
	private FlowerPart cube_r254;
	private FlowerPart cube_r255;
	private FlowerPart cube_r256;
	private FlowerPart cube_r257;
	private FlowerPart cube_r258;
	private FlowerPart cube_r259;
	private FlowerPart cube_r260;
	private FlowerPart cube_r261;
	private FlowerPart cube_r262;
	private FlowerPart cube_r263;
	private FlowerPart cube_r264;
	private FlowerPart cube_r265;
	private FlowerPart cube_r266;
	private FlowerPart cube_r267;
	private FlowerPart tepal29;
	private FlowerPart cube_r268;
	private FlowerPart cube_r269;
	private FlowerPart cube_r270;
	private FlowerPart cube_r271;
	private FlowerPart cube_r272;
	private FlowerPart cube_r273;
	private FlowerPart cube_r274;
	private FlowerPart cube_r275;
	private FlowerPart cube_r276;
	private FlowerPart cube_r277;
	private FlowerPart cube_r278;
	private FlowerPart cube_r279;
	private FlowerPart cube_r280;
	private FlowerPart cube_r281;
	private FlowerPart cube_r282;
	private FlowerPart cube_r283;
	private FlowerPart cube_r284;
	private FlowerPart cube_r285;
	private FlowerPart cube_r286;
	private FlowerPart cube_r287;
	private FlowerPart cube_r288;
	private FlowerPart cube_r289;
	private FlowerPart cube_r290;
	private FlowerPart tepal30;
	private FlowerPart cube_r291;
	private FlowerPart cube_r292;
	private FlowerPart cube_r293;
	private FlowerPart cube_r294;
	private FlowerPart cube_r295;
	private FlowerPart cube_r296;
	private FlowerPart cube_r297;
	private FlowerPart cube_r298;
	private FlowerPart cube_r299;
	private FlowerPart cube_r300;
	private FlowerPart cube_r301;
	private FlowerPart cube_r302;
	private FlowerPart cube_r303;
	private FlowerPart cube_r304;
	private FlowerPart cube_r305;
	private FlowerPart cube_r306;
	private FlowerPart cube_r307;
	private FlowerPart cube_r308;
	private FlowerPart cube_r309;
	private FlowerPart cube_r310;
	private FlowerPart cube_r311;
	private FlowerPart cube_r312;
	private FlowerPart cube_r313;
	private FlowerPart stemal25;
	private FlowerPart cube_r314;
	private FlowerPart cube_r315;
	private FlowerPart cube_r316;
	private FlowerPart cube_r317;
	private FlowerPart cube_r318;
	private FlowerPart stemal26;
	private FlowerPart cube_r319;
	private FlowerPart cube_r320;
	private FlowerPart cube_r321;
	private FlowerPart cube_r322;
	private FlowerPart cube_r323;
	private FlowerPart stemal27;
	private FlowerPart cube_r324;
	private FlowerPart cube_r325;
	private FlowerPart cube_r326;
	private FlowerPart cube_r327;
	private FlowerPart cube_r328;
	private FlowerPart stemal28;
	private FlowerPart cube_r329;
	private FlowerPart cube_r330;
	private FlowerPart cube_r331;
	private FlowerPart cube_r332;
	private FlowerPart cube_r333;
	private FlowerPart stemal29;
	private FlowerPart cube_r334;
	private FlowerPart cube_r335;
	private FlowerPart cube_r336;
	private FlowerPart cube_r337;
	private FlowerPart cube_r338;
	private FlowerPart stemal30;
	private FlowerPart cube_r339;
	private FlowerPart cube_r340;
	private FlowerPart cube_r341;
	private FlowerPart cube_r342;
	private FlowerPart cube_r343;
	private FlowerPart perianth6;
	private FlowerPart tepal31;
	private FlowerPart cube_r344;
	private FlowerPart cube_r345;
	private FlowerPart cube_r346;
	private FlowerPart cube_r347;
	private FlowerPart cube_r348;
	private FlowerPart cube_r349;
	private FlowerPart cube_r350;
	private FlowerPart cube_r351;
	private FlowerPart cube_r352;
	private FlowerPart cube_r353;
	private FlowerPart cube_r354;
	private FlowerPart cube_r355;
	private FlowerPart cube_r356;
	private FlowerPart cube_r357;
	private FlowerPart cube_r358;
	private FlowerPart cube_r359;
	private FlowerPart cube_r360;
	private FlowerPart cube_r361;
	private FlowerPart cube_r362;
	private FlowerPart cube_r363;
	private FlowerPart cube_r364;
	private FlowerPart cube_r365;
	private FlowerPart cube_r366;
	private FlowerPart tepal32;
	private FlowerPart cube_r367;
	private FlowerPart cube_r368;
	private FlowerPart cube_r369;
	private FlowerPart cube_r370;
	private FlowerPart cube_r371;
	private FlowerPart cube_r372;
	private FlowerPart cube_r373;
	private FlowerPart cube_r374;
	private FlowerPart cube_r375;
	private FlowerPart cube_r376;
	private FlowerPart cube_r377;
	private FlowerPart cube_r378;
	private FlowerPart cube_r379;
	private FlowerPart cube_r380;
	private FlowerPart cube_r381;
	private FlowerPart cube_r382;
	private FlowerPart cube_r383;
	private FlowerPart cube_r384;
	private FlowerPart cube_r385;
	private FlowerPart cube_r386;
	private FlowerPart cube_r387;
	private FlowerPart cube_r388;
	private FlowerPart cube_r389;
	private FlowerPart tepal33;
	private FlowerPart cube_r390;
	private FlowerPart cube_r391;
	private FlowerPart cube_r392;
	private FlowerPart cube_r393;
	private FlowerPart cube_r394;
	private FlowerPart cube_r395;
	private FlowerPart cube_r396;
	private FlowerPart cube_r397;
	private FlowerPart cube_r398;
	private FlowerPart cube_r399;
	private FlowerPart cube_r400;
	private FlowerPart cube_r401;
	private FlowerPart cube_r402;
	private FlowerPart cube_r403;
	private FlowerPart cube_r404;
	private FlowerPart cube_r405;
	private FlowerPart cube_r406;
	private FlowerPart cube_r407;
	private FlowerPart cube_r408;
	private FlowerPart cube_r409;
	private FlowerPart cube_r410;
	private FlowerPart cube_r411;
	private FlowerPart cube_r412;
	private FlowerPart tepal34;
	private FlowerPart cube_r413;
	private FlowerPart cube_r414;
	private FlowerPart cube_r415;
	private FlowerPart cube_r416;
	private FlowerPart cube_r417;
	private FlowerPart cube_r418;
	private FlowerPart cube_r419;
	private FlowerPart cube_r420;
	private FlowerPart cube_r421;
	private FlowerPart cube_r422;
	private FlowerPart cube_r423;
	private FlowerPart cube_r424;
	private FlowerPart cube_r425;
	private FlowerPart cube_r426;
	private FlowerPart cube_r427;
	private FlowerPart cube_r428;
	private FlowerPart cube_r429;
	private FlowerPart cube_r430;
	private FlowerPart cube_r431;
	private FlowerPart cube_r432;
	private FlowerPart cube_r433;
	private FlowerPart cube_r434;
	private FlowerPart cube_r435;
	private FlowerPart tepal35;
	private FlowerPart cube_r436;
	private FlowerPart cube_r437;
	private FlowerPart cube_r438;
	private FlowerPart cube_r439;
	private FlowerPart cube_r440;
	private FlowerPart cube_r441;
	private FlowerPart cube_r442;
	private FlowerPart cube_r443;
	private FlowerPart cube_r444;
	private FlowerPart cube_r445;
	private FlowerPart cube_r446;
	private FlowerPart cube_r447;
	private FlowerPart cube_r448;
	private FlowerPart cube_r449;
	private FlowerPart cube_r450;
	private FlowerPart cube_r451;
	private FlowerPart cube_r452;
	private FlowerPart cube_r453;
	private FlowerPart cube_r454;
	private FlowerPart cube_r455;
	private FlowerPart cube_r456;
	private FlowerPart cube_r457;
	private FlowerPart cube_r458;
	private FlowerPart tepal36;
	private FlowerPart cube_r459;
	private FlowerPart cube_r460;
	private FlowerPart cube_r461;
	private FlowerPart cube_r462;
	private FlowerPart cube_r463;
	private FlowerPart cube_r464;
	private FlowerPart cube_r465;
	private FlowerPart cube_r466;
	private FlowerPart cube_r467;
	private FlowerPart cube_r468;
	private FlowerPart cube_r469;
	private FlowerPart cube_r470;
	private FlowerPart cube_r471;
	private FlowerPart cube_r472;
	private FlowerPart cube_r473;
	private FlowerPart cube_r474;
	private FlowerPart cube_r475;
	private FlowerPart cube_r476;
	private FlowerPart cube_r477;
	private FlowerPart cube_r478;
	private FlowerPart cube_r479;
	private FlowerPart cube_r480;
	private FlowerPart cube_r481;
	private FlowerPart stemal31;
	private FlowerPart cube_r482;
	private FlowerPart cube_r483;
	private FlowerPart cube_r484;
	private FlowerPart cube_r485;
	private FlowerPart cube_r486;
	private FlowerPart stemal32;
	private FlowerPart cube_r487;
	private FlowerPart cube_r488;
	private FlowerPart cube_r489;
	private FlowerPart cube_r490;
	private FlowerPart cube_r491;
	private FlowerPart stemal33;
	private FlowerPart cube_r492;
	private FlowerPart cube_r493;
	private FlowerPart cube_r494;
	private FlowerPart cube_r495;
	private FlowerPart cube_r496;
	private FlowerPart stemal34;
	private FlowerPart cube_r497;
	private FlowerPart cube_r498;
	private FlowerPart cube_r499;
	private FlowerPart cube_r500;
	private FlowerPart cube_r501;
	private FlowerPart stemal35;
	private FlowerPart cube_r502;
	private FlowerPart cube_r503;
	private FlowerPart cube_r504;
	private FlowerPart cube_r505;
	private FlowerPart cube_r506;
	private FlowerPart stemal36;
	private FlowerPart cube_r507;
	private FlowerPart cube_r508;
	private FlowerPart cube_r509;
	private FlowerPart cube_r510;
	private FlowerPart cube_r511;
	private FlowerPart half2;
	private FlowerPart perianth3;
	private FlowerPart tepal13;
	private FlowerPart cube_r512;
	private FlowerPart cube_r513;
	private FlowerPart cube_r514;
	private FlowerPart cube_r515;
	private FlowerPart cube_r516;
	private FlowerPart cube_r517;
	private FlowerPart cube_r518;
	private FlowerPart cube_r519;
	private FlowerPart cube_r520;
	private FlowerPart cube_r521;
	private FlowerPart cube_r522;
	private FlowerPart cube_r523;
	private FlowerPart cube_r524;
	private FlowerPart cube_r525;
	private FlowerPart cube_r526;
	private FlowerPart cube_r527;
	private FlowerPart cube_r528;
	private FlowerPart cube_r529;
	private FlowerPart cube_r530;
	private FlowerPart cube_r531;
	private FlowerPart cube_r532;
	private FlowerPart cube_r533;
	private FlowerPart cube_r534;
	private FlowerPart tepal14;
	private FlowerPart cube_r535;
	private FlowerPart cube_r536;
	private FlowerPart cube_r537;
	private FlowerPart cube_r538;
	private FlowerPart cube_r539;
	private FlowerPart cube_r540;
	private FlowerPart cube_r541;
	private FlowerPart cube_r542;
	private FlowerPart cube_r543;
	private FlowerPart cube_r544;
	private FlowerPart cube_r545;
	private FlowerPart cube_r546;
	private FlowerPart cube_r547;
	private FlowerPart cube_r548;
	private FlowerPart cube_r549;
	private FlowerPart cube_r550;
	private FlowerPart cube_r551;
	private FlowerPart cube_r552;
	private FlowerPart cube_r553;
	private FlowerPart cube_r554;
	private FlowerPart cube_r555;
	private FlowerPart cube_r556;
	private FlowerPart cube_r557;
	private FlowerPart tepal15;
	private FlowerPart cube_r558;
	private FlowerPart cube_r559;
	private FlowerPart cube_r560;
	private FlowerPart cube_r561;
	private FlowerPart cube_r562;
	private FlowerPart cube_r563;
	private FlowerPart cube_r564;
	private FlowerPart cube_r565;
	private FlowerPart cube_r566;
	private FlowerPart cube_r567;
	private FlowerPart cube_r568;
	private FlowerPart cube_r569;
	private FlowerPart cube_r570;
	private FlowerPart cube_r571;
	private FlowerPart cube_r572;
	private FlowerPart cube_r573;
	private FlowerPart cube_r574;
	private FlowerPart cube_r575;
	private FlowerPart cube_r576;
	private FlowerPart cube_r577;
	private FlowerPart cube_r578;
	private FlowerPart cube_r579;
	private FlowerPart cube_r580;
	private FlowerPart tepal16;
	private FlowerPart cube_r581;
	private FlowerPart cube_r582;
	private FlowerPart cube_r583;
	private FlowerPart cube_r584;
	private FlowerPart cube_r585;
	private FlowerPart cube_r586;
	private FlowerPart cube_r587;
	private FlowerPart cube_r588;
	private FlowerPart cube_r589;
	private FlowerPart cube_r590;
	private FlowerPart cube_r591;
	private FlowerPart cube_r592;
	private FlowerPart cube_r593;
	private FlowerPart cube_r594;
	private FlowerPart cube_r595;
	private FlowerPart cube_r596;
	private FlowerPart cube_r597;
	private FlowerPart cube_r598;
	private FlowerPart cube_r599;
	private FlowerPart cube_r600;
	private FlowerPart cube_r601;
	private FlowerPart cube_r602;
	private FlowerPart cube_r603;
	private FlowerPart tepal17;
	private FlowerPart cube_r604;
	private FlowerPart cube_r605;
	private FlowerPart cube_r606;
	private FlowerPart cube_r607;
	private FlowerPart cube_r608;
	private FlowerPart cube_r609;
	private FlowerPart cube_r610;
	private FlowerPart cube_r611;
	private FlowerPart cube_r612;
	private FlowerPart cube_r613;
	private FlowerPart cube_r614;
	private FlowerPart cube_r615;
	private FlowerPart cube_r616;
	private FlowerPart cube_r617;
	private FlowerPart cube_r618;
	private FlowerPart cube_r619;
	private FlowerPart cube_r620;
	private FlowerPart cube_r621;
	private FlowerPart cube_r622;
	private FlowerPart cube_r623;
	private FlowerPart cube_r624;
	private FlowerPart cube_r625;
	private FlowerPart cube_r626;
	private FlowerPart tepal18;
	private FlowerPart cube_r627;
	private FlowerPart cube_r628;
	private FlowerPart cube_r629;
	private FlowerPart cube_r630;
	private FlowerPart cube_r631;
	private FlowerPart cube_r632;
	private FlowerPart cube_r633;
	private FlowerPart cube_r634;
	private FlowerPart cube_r635;
	private FlowerPart cube_r636;
	private FlowerPart cube_r637;
	private FlowerPart cube_r638;
	private FlowerPart cube_r639;
	private FlowerPart cube_r640;
	private FlowerPart cube_r641;
	private FlowerPart cube_r642;
	private FlowerPart cube_r643;
	private FlowerPart cube_r644;
	private FlowerPart cube_r645;
	private FlowerPart cube_r646;
	private FlowerPart cube_r647;
	private FlowerPart cube_r648;
	private FlowerPart cube_r649;
	private FlowerPart stemal13;
	private FlowerPart cube_r650;
	private FlowerPart cube_r651;
	private FlowerPart cube_r652;
	private FlowerPart cube_r653;
	private FlowerPart cube_r654;
	private FlowerPart stemal14;
	private FlowerPart cube_r655;
	private FlowerPart cube_r656;
	private FlowerPart cube_r657;
	private FlowerPart cube_r658;
	private FlowerPart cube_r659;
	private FlowerPart stemal15;
	private FlowerPart cube_r660;
	private FlowerPart cube_r661;
	private FlowerPart cube_r662;
	private FlowerPart cube_r663;
	private FlowerPart cube_r664;
	private FlowerPart stemal16;
	private FlowerPart cube_r665;
	private FlowerPart cube_r666;
	private FlowerPart cube_r667;
	private FlowerPart cube_r668;
	private FlowerPart cube_r669;
	private FlowerPart stemal17;
	private FlowerPart cube_r670;
	private FlowerPart cube_r671;
	private FlowerPart cube_r672;
	private FlowerPart cube_r673;
	private FlowerPart cube_r674;
	private FlowerPart stemal18;
	private FlowerPart cube_r675;
	private FlowerPart cube_r676;
	private FlowerPart cube_r677;
	private FlowerPart cube_r678;
	private FlowerPart cube_r679;
	private FlowerPart perianth2;
	private FlowerPart tepal7;
	private FlowerPart cube_r680;
	private FlowerPart cube_r681;
	private FlowerPart cube_r682;
	private FlowerPart cube_r683;
	private FlowerPart cube_r684;
	private FlowerPart cube_r685;
	private FlowerPart cube_r686;
	private FlowerPart cube_r687;
	private FlowerPart cube_r688;
	private FlowerPart cube_r689;
	private FlowerPart cube_r690;
	private FlowerPart cube_r691;
	private FlowerPart cube_r692;
	private FlowerPart cube_r693;
	private FlowerPart cube_r694;
	private FlowerPart cube_r695;
	private FlowerPart cube_r696;
	private FlowerPart cube_r697;
	private FlowerPart cube_r698;
	private FlowerPart cube_r699;
	private FlowerPart cube_r700;
	private FlowerPart cube_r701;
	private FlowerPart cube_r702;
	private FlowerPart tepal8;
	private FlowerPart cube_r703;
	private FlowerPart cube_r704;
	private FlowerPart cube_r705;
	private FlowerPart cube_r706;
	private FlowerPart cube_r707;
	private FlowerPart cube_r708;
	private FlowerPart cube_r709;
	private FlowerPart cube_r710;
	private FlowerPart cube_r711;
	private FlowerPart cube_r712;
	private FlowerPart cube_r713;
	private FlowerPart cube_r714;
	private FlowerPart cube_r715;
	private FlowerPart cube_r716;
	private FlowerPart cube_r717;
	private FlowerPart cube_r718;
	private FlowerPart cube_r719;
	private FlowerPart cube_r720;
	private FlowerPart cube_r721;
	private FlowerPart cube_r722;
	private FlowerPart cube_r723;
	private FlowerPart cube_r724;
	private FlowerPart cube_r725;
	private FlowerPart tepal9;
	private FlowerPart cube_r726;
	private FlowerPart cube_r727;
	private FlowerPart cube_r728;
	private FlowerPart cube_r729;
	private FlowerPart cube_r730;
	private FlowerPart cube_r731;
	private FlowerPart cube_r732;
	private FlowerPart cube_r733;
	private FlowerPart cube_r734;
	private FlowerPart cube_r735;
	private FlowerPart cube_r736;
	private FlowerPart cube_r737;
	private FlowerPart cube_r738;
	private FlowerPart cube_r739;
	private FlowerPart cube_r740;
	private FlowerPart cube_r741;
	private FlowerPart cube_r742;
	private FlowerPart cube_r743;
	private FlowerPart cube_r744;
	private FlowerPart cube_r745;
	private FlowerPart cube_r746;
	private FlowerPart cube_r747;
	private FlowerPart cube_r748;
	private FlowerPart tepal10;
	private FlowerPart cube_r749;
	private FlowerPart cube_r750;
	private FlowerPart cube_r751;
	private FlowerPart cube_r752;
	private FlowerPart cube_r753;
	private FlowerPart cube_r754;
	private FlowerPart cube_r755;
	private FlowerPart cube_r756;
	private FlowerPart cube_r757;
	private FlowerPart cube_r758;
	private FlowerPart cube_r759;
	private FlowerPart cube_r760;
	private FlowerPart cube_r761;
	private FlowerPart cube_r762;
	private FlowerPart cube_r763;
	private FlowerPart cube_r764;
	private FlowerPart cube_r765;
	private FlowerPart cube_r766;
	private FlowerPart cube_r767;
	private FlowerPart cube_r768;
	private FlowerPart cube_r769;
	private FlowerPart cube_r770;
	private FlowerPart cube_r771;
	private FlowerPart tepal11;
	private FlowerPart cube_r772;
	private FlowerPart cube_r773;
	private FlowerPart cube_r774;
	private FlowerPart cube_r775;
	private FlowerPart cube_r776;
	private FlowerPart cube_r777;
	private FlowerPart cube_r778;
	private FlowerPart cube_r779;
	private FlowerPart cube_r780;
	private FlowerPart cube_r781;
	private FlowerPart cube_r782;
	private FlowerPart cube_r783;
	private FlowerPart cube_r784;
	private FlowerPart cube_r785;
	private FlowerPart cube_r786;
	private FlowerPart cube_r787;
	private FlowerPart cube_r788;
	private FlowerPart cube_r789;
	private FlowerPart cube_r790;
	private FlowerPart cube_r791;
	private FlowerPart cube_r792;
	private FlowerPart cube_r793;
	private FlowerPart cube_r794;
	private FlowerPart tepal12;
	private FlowerPart cube_r795;
	private FlowerPart cube_r796;
	private FlowerPart cube_r797;
	private FlowerPart cube_r798;
	private FlowerPart cube_r799;
	private FlowerPart cube_r800;
	private FlowerPart cube_r801;
	private FlowerPart cube_r802;
	private FlowerPart cube_r803;
	private FlowerPart cube_r804;
	private FlowerPart cube_r805;
	private FlowerPart cube_r806;
	private FlowerPart cube_r807;
	private FlowerPart cube_r808;
	private FlowerPart cube_r809;
	private FlowerPart cube_r810;
	private FlowerPart cube_r811;
	private FlowerPart cube_r812;
	private FlowerPart cube_r813;
	private FlowerPart cube_r814;
	private FlowerPart cube_r815;
	private FlowerPart cube_r816;
	private FlowerPart cube_r817;
	private FlowerPart stemal7;
	private FlowerPart cube_r818;
	private FlowerPart cube_r819;
	private FlowerPart cube_r820;
	private FlowerPart cube_r821;
	private FlowerPart cube_r822;
	private FlowerPart stemal8;
	private FlowerPart cube_r823;
	private FlowerPart cube_r824;
	private FlowerPart cube_r825;
	private FlowerPart cube_r826;
	private FlowerPart cube_r827;
	private FlowerPart stemal9;
	private FlowerPart cube_r828;
	private FlowerPart cube_r829;
	private FlowerPart cube_r830;
	private FlowerPart cube_r831;
	private FlowerPart cube_r832;
	private FlowerPart stemal10;
	private FlowerPart cube_r833;
	private FlowerPart cube_r834;
	private FlowerPart cube_r835;
	private FlowerPart cube_r836;
	private FlowerPart cube_r837;
	private FlowerPart stemal11;
	private FlowerPart cube_r838;
	private FlowerPart cube_r839;
	private FlowerPart cube_r840;
	private FlowerPart cube_r841;
	private FlowerPart cube_r842;
	private FlowerPart stemal12;
	private FlowerPart cube_r843;
	private FlowerPart cube_r844;
	private FlowerPart cube_r845;
	private FlowerPart cube_r846;
	private FlowerPart cube_r847;
	private FlowerPart perianth;
	private FlowerPart tepal1;
	private FlowerPart cube_r848;
	private FlowerPart cube_r849;
	private FlowerPart cube_r850;
	private FlowerPart cube_r851;
	private FlowerPart cube_r852;
	private FlowerPart cube_r853;
	private FlowerPart cube_r854;
	private FlowerPart cube_r855;
	private FlowerPart cube_r856;
	private FlowerPart cube_r857;
	private FlowerPart cube_r858;
	private FlowerPart cube_r859;
	private FlowerPart cube_r860;
	private FlowerPart cube_r861;
	private FlowerPart cube_r862;
	private FlowerPart cube_r863;
	private FlowerPart cube_r864;
	private FlowerPart cube_r865;
	private FlowerPart cube_r866;
	private FlowerPart cube_r867;
	private FlowerPart cube_r868;
	private FlowerPart cube_r869;
	private FlowerPart cube_r870;
	private FlowerPart tepal2;
	private FlowerPart cube_r871;
	private FlowerPart cube_r872;
	private FlowerPart cube_r873;
	private FlowerPart cube_r874;
	private FlowerPart cube_r875;
	private FlowerPart cube_r876;
	private FlowerPart cube_r877;
	private FlowerPart cube_r878;
	private FlowerPart cube_r879;
	private FlowerPart cube_r880;
	private FlowerPart cube_r881;
	private FlowerPart cube_r882;
	private FlowerPart cube_r883;
	private FlowerPart cube_r884;
	private FlowerPart cube_r885;
	private FlowerPart cube_r886;
	private FlowerPart cube_r887;
	private FlowerPart cube_r888;
	private FlowerPart cube_r889;
	private FlowerPart cube_r890;
	private FlowerPart cube_r891;
	private FlowerPart cube_r892;
	private FlowerPart cube_r893;
	private FlowerPart tepal3;
	private FlowerPart cube_r894;
	private FlowerPart cube_r895;
	private FlowerPart cube_r896;
	private FlowerPart cube_r897;
	private FlowerPart cube_r898;
	private FlowerPart cube_r899;
	private FlowerPart cube_r900;
	private FlowerPart cube_r901;
	private FlowerPart cube_r902;
	private FlowerPart cube_r903;
	private FlowerPart cube_r904;
	private FlowerPart cube_r905;
	private FlowerPart cube_r906;
	private FlowerPart cube_r907;
	private FlowerPart cube_r908;
	private FlowerPart cube_r909;
	private FlowerPart cube_r910;
	private FlowerPart cube_r911;
	private FlowerPart cube_r912;
	private FlowerPart cube_r913;
	private FlowerPart cube_r914;
	private FlowerPart cube_r915;
	private FlowerPart cube_r916;
	private FlowerPart tepal4;
	private FlowerPart cube_r917;
	private FlowerPart cube_r918;
	private FlowerPart cube_r919;
	private FlowerPart cube_r920;
	private FlowerPart cube_r921;
	private FlowerPart cube_r922;
	private FlowerPart cube_r923;
	private FlowerPart cube_r924;
	private FlowerPart cube_r925;
	private FlowerPart cube_r926;
	private FlowerPart cube_r927;
	private FlowerPart cube_r928;
	private FlowerPart cube_r929;
	private FlowerPart cube_r930;
	private FlowerPart cube_r931;
	private FlowerPart cube_r932;
	private FlowerPart cube_r933;
	private FlowerPart cube_r934;
	private FlowerPart cube_r935;
	private FlowerPart cube_r936;
	private FlowerPart cube_r937;
	private FlowerPart cube_r938;
	private FlowerPart cube_r939;
	private FlowerPart tepal5;
	private FlowerPart cube_r940;
	private FlowerPart cube_r941;
	private FlowerPart cube_r942;
	private FlowerPart cube_r943;
	private FlowerPart cube_r944;
	private FlowerPart cube_r945;
	private FlowerPart cube_r946;
	private FlowerPart cube_r947;
	private FlowerPart cube_r948;
	private FlowerPart cube_r949;
	private FlowerPart cube_r950;
	private FlowerPart cube_r951;
	private FlowerPart cube_r952;
	private FlowerPart cube_r953;
	private FlowerPart cube_r954;
	private FlowerPart cube_r955;
	private FlowerPart cube_r956;
	private FlowerPart cube_r957;
	private FlowerPart cube_r958;
	private FlowerPart cube_r959;
	private FlowerPart cube_r960;
	private FlowerPart cube_r961;
	private FlowerPart cube_r962;
	private FlowerPart tepal6;
	private FlowerPart cube_r963;
	private FlowerPart cube_r964;
	private FlowerPart cube_r965;
	private FlowerPart cube_r966;
	private FlowerPart cube_r967;
	private FlowerPart cube_r968;
	private FlowerPart cube_r969;
	private FlowerPart cube_r970;
	private FlowerPart cube_r971;
	private FlowerPart cube_r972;
	private FlowerPart cube_r973;
	private FlowerPart cube_r974;
	private FlowerPart cube_r975;
	private FlowerPart cube_r976;
	private FlowerPart cube_r977;
	private FlowerPart cube_r978;
	private FlowerPart cube_r979;
	private FlowerPart cube_r980;
	private FlowerPart cube_r981;
	private FlowerPart cube_r982;
	private FlowerPart cube_r983;
	private FlowerPart cube_r984;
	private FlowerPart cube_r985;
	private FlowerPart stemal;
	private FlowerPart cube_r986;
	private FlowerPart cube_r987;
	private FlowerPart cube_r988;
	private FlowerPart cube_r989;
	private FlowerPart cube_r990;
	private FlowerPart stemal2;
	private FlowerPart cube_r991;
	private FlowerPart cube_r992;
	private FlowerPart cube_r993;
	private FlowerPart cube_r994;
	private FlowerPart cube_r995;
	private FlowerPart stemal3;
	private FlowerPart cube_r996;
	private FlowerPart cube_r997;
	private FlowerPart cube_r998;
	private FlowerPart cube_r999;
	private FlowerPart cube_r1000;
	private FlowerPart stemal5;
	private FlowerPart cube_r1001;
	private FlowerPart cube_r1002;
	private FlowerPart cube_r1003;
	private FlowerPart cube_r1004;
	private FlowerPart cube_r1005;
	private FlowerPart stemal6;
	private FlowerPart cube_r1006;
	private FlowerPart cube_r1007;
	private FlowerPart cube_r1008;
	private FlowerPart cube_r1009;
	private FlowerPart cube_r1010;
	private FlowerPart stemal4;
	private FlowerPart cube_r1011;
	private FlowerPart cube_r1012;
	private FlowerPart cube_r1013;
	private FlowerPart cube_r1014;
	private FlowerPart cube_r1015;

	public ModelLycoris() {
		init0();
		init1();
		init2();
		init3();
		init4();
		init5();
		init6();
		init7();
		init8();
		init9();
		init10();
		init11();
		init12();
		init13();
		init14();
		init15();
		init16();
		init17();
		init18();
		init19();
		init20();
		init21();
		init22();
		init23();
		init24();
		init25();
		init26();
		init27();
		init28();
		init29();
		init30();
		init31();
		init32();
		init33();
		init34();
		init35();
		init36();
		init37();
		init38();
		init39();
		init40();
		init41();
		init42();
		init43();
		init44();
		init45();
		init46();
		init47();
		init48();
		init49();
		init50();
		init51();
		init52();
		init53();
		init54();
		init55();
		init56();
		init57();
		init58();
		init59();
		init60();
		init61();
		init62();
		init63();
		init64();
		init65();
		init66();
		init67();
		init68();
		init69();
		init70();
		init71();
		init72();
		init73();
		init74();
		init75();
		init76();
		init77();
		init78();
		init79();
		init80();
		init81();
		init82();
		init83();
		init84();
		init85();
		init86();
		init87();
		init88();
		init89();
		init90();
		init91();
		init92();
		init93();
		init94();
		init95();
		init96();
		init97();
		init98();
		init99();
		init100();
		init101();
		init102();
		init103();
		init104();
		init105();
		init106();
		init107();
		init108();
		init109();
		init110();
		init111();
		init112();
		init113();
		init114();
		init115();
		init116();
		init117();
		init118();
		init119();
		init120();
		init121();
		init122();
		init123();
		init124();
		init125();
		init126();
		init127();
		init128();
		init129();
		init130();
		init131();
		init132();
		init133();
		init134();
		init135();
		init136();
		init137();
		init138();
		init139();
		init140();
		init141();
		init142();
		init143();
		init144();
		init145();
		init146();
		init147();
		init148();
		init149();
		init150();
		init151();
		init152();
		init153();
		init154();
		init155();
		init156();
		init157();
		init158();
		init159();
		init160();
		init161();
		init162();
		init163();
		init164();
		init165();
		init166();
		init167();
		init168();
		init169();
		init170();
	}

	private void init0() {
		textureWidth = 16;
		textureHeight = 16;

		stem = new FlowerPart(this);
		stem.setRotationPoint(0.0F, -24.0F, 0.0F);
		stem.floatCubes.add(new FloatCube(0, 0, -1.0F, 0.0F, 0.0F, 1.0F, 48.0F, 1.0F, 0.0F, false));

		group = new FlowerPart(this);
		group.setRotationPoint(0.0F, 0.0F, 0.0F);
		stem.addChild(group);
		

		cube_r1 = new FlowerPart(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		group.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.829F, 0.0F, 0.0F);
		cube_r1.floatCubes.add(new FloatCube(0, 0, -1.0F, -18.0F, 0.0F, 1.0F, 18.0F, 1.0F, 0.0F, false));

		group2 = new FlowerPart(this);
		group2.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group2);
		

		cube_r2 = new FlowerPart(this);
		cube_r2.setRotationPoint(0.485F, 0.0F, -0.49F);
		group2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.6545F, 0.0F, 0.0F);
		cube_r2.floatCubes.add(new FloatCube(4, 0, -1.0F, -18.0F, 0.0F, 1.0F, 18.0F, 1.0F, 0.0F, false));

		group3 = new FlowerPart(this);
		group3.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group3);
		setRotationAngle(group3, 0.0F, 1.0472F, 0.0F);
		

		cube_r3 = new FlowerPart(this);
		cube_r3.setRotationPoint(0.485F, 0.0F, -0.49F);
		group3.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.6545F, 0.0F, 0.0F);
		cube_r3.floatCubes.add(new FloatCube(8, 0, -1.0F, -18.0F, 0.0F, 1.0F, 18.0F, 1.0F, 0.0F, false));
	}

	private void init1() {

		group4 = new FlowerPart(this);
		group4.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group4);
		setRotationAngle(group4, 0.0F, 2.0944F, 0.0F);
		

		cube_r4 = new FlowerPart(this);
		cube_r4.setRotationPoint(0.485F, 0.0F, -0.49F);
		group4.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.6545F, 0.0F, 0.0F);
		cube_r4.floatCubes.add(new FloatCube(8, 19, -1.0F, -15.7F, 0.0F, 1.0F, 15.7F, 1.0F, 0.0F, false));

		group5 = new FlowerPart(this);
		group5.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group5);
		setRotationAngle(group5, 0.0F, 3.1416F, 0.0F);
		

		cube_r5 = new FlowerPart(this);
		cube_r5.setRotationPoint(0.485F, 0.0F, -0.49F);
		group5.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.7854F, 0.0F, 0.0F);
		cube_r5.floatCubes.add(new FloatCube(4, 19, -1.0F, -17.135F, 0.0F, 1.0F, 17.135F, 1.0F, 0.0F, false));

		group6 = new FlowerPart(this);
		group6.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group6);
		setRotationAngle(group6, 0.0F, -2.0944F, 0.0F);
		

		cube_r6 = new FlowerPart(this);
		cube_r6.setRotationPoint(0.485F, 0.0F, -0.49F);
		group6.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.6545F, 0.0F, 0.0F);
		cube_r6.floatCubes.add(new FloatCube(12, 0, -1.0F, -18.0F, 0.0F, 1.0F, 18.0F, 1.0F, 0.0F, false));

		group7 = new FlowerPart(this);
		group7.setRotationPoint(-0.485F, 0.0F, 0.49F);
		stem.addChild(group7);
	}

	private void init2() {
		setRotationAngle(group7, 0.0F, -1.0472F, 0.0F);
		

		cube_r7 = new FlowerPart(this);
		cube_r7.setRotationPoint(0.485F, 0.0F, -0.49F);
		group7.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.6545F, 0.0F, 0.0F);
		cube_r7.floatCubes.add(new FloatCube(16, 0, -1.0F, -18.0F, 0.0F, 1.0F, 18.0F, 1.0F, 0.0F, false));

		flower = new FlowerPart(this);
		flower.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		half1 = new FlowerPart(this);
		half1.setRotationPoint(-11.435F, -59.84F, -5.21F);
		flower.addChild(half1);
		setRotationAngle(half1, 0.0F, 3.1416F, 0.0F);
		

		perianth4 = new FlowerPart(this);
		perianth4.setRotationPoint(-2.015F, -2.0F, -0.255F);
		half1.addChild(perianth4);
		setRotationAngle(perianth4, -2.9739F, 0.2748F, -2.5667F);
		

		tepal19 = new FlowerPart(this);
		tepal19.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal19);
		setRotationAngle(tepal19, 0.0F, 0.0F, -0.6109F);
		

		cube_r8 = new FlowerPart(this);
		cube_r8.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal19.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.0F, 0.0F, 0.2574F);
		cube_r8.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r9 = new FlowerPart(this);
		cube_r9.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal19.addChild(cube_r9);
	}

	private void init3() {
		setRotationAngle(cube_r9, -0.0167F, 0.1453F, 0.7743F);
		cube_r9.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r10 = new FlowerPart(this);
		cube_r10.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal19.addChild(cube_r10);
		setRotationAngle(cube_r10, -0.0167F, -0.1339F, 0.7789F);
		cube_r10.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r11 = new FlowerPart(this);
		cube_r11.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal19.addChild(cube_r11);
		setRotationAngle(cube_r11, -0.0167F, -0.1339F, 0.7789F);
		cube_r11.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r12 = new FlowerPart(this);
		cube_r12.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal19.addChild(cube_r12);
		setRotationAngle(cube_r12, -0.0165F, 0.0057F, 0.7766F);
		cube_r12.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r12.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r12.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r13 = new FlowerPart(this);
		cube_r13.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal19.addChild(cube_r13);
		setRotationAngle(cube_r13, -0.0139F, -0.0105F, -0.2006F);
		cube_r13.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r14 = new FlowerPart(this);
		cube_r14.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal19.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0015F, -0.0174F, -1.213F);
		cube_r14.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r15 = new FlowerPart(this);
		cube_r15.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal19.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, 0.1047F, -2.4696F);
		cube_r15.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init4() {

		cube_r16 = new FlowerPart(this);
		cube_r16.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal19.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.0F, -0.1047F, -2.4696F);
		cube_r16.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r17 = new FlowerPart(this);
		cube_r17.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal19.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0F, 0.0F, -2.4696F);
		cube_r17.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r17.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r17.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r18 = new FlowerPart(this);
		cube_r18.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal19.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.0F, 0.0F, 2.8536F);
		cube_r18.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r19 = new FlowerPart(this);
		cube_r19.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal19.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.0F, -0.0873F, 1.8588F);
		cube_r19.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r20 = new FlowerPart(this);
		cube_r20.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal19.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, 0.1309F, 1.8588F);
		cube_r20.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r21 = new FlowerPart(this);
		cube_r21.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal19.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, 0.0F, 1.8588F);
		cube_r21.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r21.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r22 = new FlowerPart(this);
	}

	private void init5() {
		cube_r22.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal19.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, 0.0F, 1.8588F);
		cube_r22.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r23 = new FlowerPart(this);
		cube_r23.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal19.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0F, 0.0F, 1.0036F);
		cube_r23.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r24 = new FlowerPart(this);
		cube_r24.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal19.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, -0.0873F, 0.5236F);
		cube_r24.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r25 = new FlowerPart(this);
		cube_r25.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal19.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, 0.0873F, 0.5236F);
		cube_r25.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r26 = new FlowerPart(this);
		cube_r26.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal19.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, 0.0F, 0.5236F);
		cube_r26.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r26.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r27 = new FlowerPart(this);
		cube_r27.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal19.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 0.0F, -0.1745F);
		cube_r27.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r28 = new FlowerPart(this);
		cube_r28.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal19.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0F, -0.2618F, -0.1745F);
	}

	private void init6() {
		cube_r28.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r29 = new FlowerPart(this);
		cube_r29.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal19.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, 0.2618F, -0.1745F);
		cube_r29.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r30 = new FlowerPart(this);
		cube_r30.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal19.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.0F, 0.0F, -0.1745F);
		cube_r30.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal20 = new FlowerPart(this);
		tepal20.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal20);
		setRotationAngle(tepal20, -0.5056F, 0.9128F, -0.8547F);
		

		cube_r31 = new FlowerPart(this);
		cube_r31.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal20.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.0F, 0.0F, 0.2574F);
		cube_r31.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r32 = new FlowerPart(this);
		cube_r32.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal20.addChild(cube_r32);
		setRotationAngle(cube_r32, -0.0167F, 0.1453F, 0.7743F);
		cube_r32.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r33 = new FlowerPart(this);
		cube_r33.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal20.addChild(cube_r33);
		setRotationAngle(cube_r33, -0.0167F, -0.1339F, 0.7789F);
		cube_r33.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r34 = new FlowerPart(this);
		cube_r34.setRotationPoint(8.955F, 3.28F, -0.245F);
	}

	private void init7() {
		tepal20.addChild(cube_r34);
		setRotationAngle(cube_r34, -0.0167F, -0.1339F, 0.7789F);
		cube_r34.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r35 = new FlowerPart(this);
		cube_r35.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal20.addChild(cube_r35);
		setRotationAngle(cube_r35, -0.0165F, 0.0057F, 0.7766F);
		cube_r35.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r35.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r35.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r36 = new FlowerPart(this);
		cube_r36.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal20.addChild(cube_r36);
		setRotationAngle(cube_r36, -0.0139F, -0.0105F, -0.2006F);
		cube_r36.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r37 = new FlowerPart(this);
		cube_r37.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal20.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.0015F, -0.0174F, -1.213F);
		cube_r37.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r38 = new FlowerPart(this);
		cube_r38.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal20.addChild(cube_r38);
		setRotationAngle(cube_r38, 0.0F, 0.1047F, -2.4696F);
		cube_r38.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r39 = new FlowerPart(this);
		cube_r39.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal20.addChild(cube_r39);
		setRotationAngle(cube_r39, 0.0F, -0.1047F, -2.4696F);
		cube_r39.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r40 = new FlowerPart(this);
		cube_r40.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal20.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.0F, 0.0F, -2.4696F);
	}

	private void init8() {
		cube_r40.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r40.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r40.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r41 = new FlowerPart(this);
		cube_r41.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal20.addChild(cube_r41);
		setRotationAngle(cube_r41, 0.0F, 0.0F, 2.8536F);
		cube_r41.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r42 = new FlowerPart(this);
		cube_r42.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal20.addChild(cube_r42);
		setRotationAngle(cube_r42, 0.0F, -0.0873F, 1.8588F);
		cube_r42.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r43 = new FlowerPart(this);
		cube_r43.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal20.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0F, 0.1309F, 1.8588F);
		cube_r43.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r44 = new FlowerPart(this);
		cube_r44.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal20.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, 0.0F, 1.8588F);
		cube_r44.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r44.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r45 = new FlowerPart(this);
		cube_r45.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal20.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, 0.0F, 1.8588F);
		cube_r45.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r46 = new FlowerPart(this);
		cube_r46.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal20.addChild(cube_r46);
		setRotationAngle(cube_r46, 0.0F, 0.0F, 1.0036F);
		cube_r46.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));
	}

	private void init9() {

		cube_r47 = new FlowerPart(this);
		cube_r47.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal20.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.0F, -0.0873F, 0.5236F);
		cube_r47.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r48 = new FlowerPart(this);
		cube_r48.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal20.addChild(cube_r48);
		setRotationAngle(cube_r48, 0.0F, 0.0873F, 0.5236F);
		cube_r48.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r49 = new FlowerPart(this);
		cube_r49.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal20.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.0F, 0.0F, 0.5236F);
		cube_r49.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r49.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r50 = new FlowerPart(this);
		cube_r50.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal20.addChild(cube_r50);
		setRotationAngle(cube_r50, 0.0F, 0.0F, -0.1745F);
		cube_r50.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r51 = new FlowerPart(this);
		cube_r51.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal20.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, -0.2618F, -0.1745F);
		cube_r51.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r52 = new FlowerPart(this);
		cube_r52.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal20.addChild(cube_r52);
		setRotationAngle(cube_r52, 0.0F, 0.2618F, -0.1745F);
		cube_r52.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r53 = new FlowerPart(this);
		cube_r53.setRotationPoint(1.27F, -1.05F, -0.6F);
	}

	private void init10() {
		tepal20.addChild(cube_r53);
		setRotationAngle(cube_r53, 0.0F, 0.0F, -0.1745F);
		cube_r53.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal21 = new FlowerPart(this);
		tepal21.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal21);
		setRotationAngle(tepal21, -2.6068F, 0.9507F, -2.7742F);
		

		cube_r54 = new FlowerPart(this);
		cube_r54.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal21.addChild(cube_r54);
		setRotationAngle(cube_r54, 0.0F, 0.0F, 0.2574F);
		cube_r54.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r55 = new FlowerPart(this);
		cube_r55.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal21.addChild(cube_r55);
		setRotationAngle(cube_r55, -0.0167F, 0.1453F, 0.7743F);
		cube_r55.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r56 = new FlowerPart(this);
		cube_r56.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal21.addChild(cube_r56);
		setRotationAngle(cube_r56, -0.0167F, -0.1339F, 0.7789F);
		cube_r56.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r57 = new FlowerPart(this);
		cube_r57.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal21.addChild(cube_r57);
		setRotationAngle(cube_r57, -0.0167F, -0.1339F, 0.7789F);
		cube_r57.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r58 = new FlowerPart(this);
		cube_r58.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal21.addChild(cube_r58);
		setRotationAngle(cube_r58, -0.0165F, 0.0057F, 0.7766F);
		cube_r58.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r58.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
	}

	private void init11() {
		cube_r58.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r59 = new FlowerPart(this);
		cube_r59.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal21.addChild(cube_r59);
		setRotationAngle(cube_r59, -0.0139F, -0.0105F, -0.2006F);
		cube_r59.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r60 = new FlowerPart(this);
		cube_r60.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal21.addChild(cube_r60);
		setRotationAngle(cube_r60, 0.0015F, -0.0174F, -1.213F);
		cube_r60.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r61 = new FlowerPart(this);
		cube_r61.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal21.addChild(cube_r61);
		setRotationAngle(cube_r61, 0.0F, 0.1047F, -2.4696F);
		cube_r61.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r62 = new FlowerPart(this);
		cube_r62.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal21.addChild(cube_r62);
		setRotationAngle(cube_r62, 0.0F, -0.1047F, -2.4696F);
		cube_r62.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r63 = new FlowerPart(this);
		cube_r63.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal21.addChild(cube_r63);
		setRotationAngle(cube_r63, 0.0F, 0.0F, -2.4696F);
		cube_r63.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r63.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r63.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r64 = new FlowerPart(this);
		cube_r64.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal21.addChild(cube_r64);
		setRotationAngle(cube_r64, 0.0F, 0.0F, 2.8536F);
		cube_r64.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r65 = new FlowerPart(this);
	}

	private void init12() {
		cube_r65.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal21.addChild(cube_r65);
		setRotationAngle(cube_r65, 0.0F, -0.0873F, 1.8588F);
		cube_r65.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r66 = new FlowerPart(this);
		cube_r66.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal21.addChild(cube_r66);
		setRotationAngle(cube_r66, 0.0F, 0.1309F, 1.8588F);
		cube_r66.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r67 = new FlowerPart(this);
		cube_r67.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal21.addChild(cube_r67);
		setRotationAngle(cube_r67, 0.0F, 0.0F, 1.8588F);
		cube_r67.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r67.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r68 = new FlowerPart(this);
		cube_r68.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal21.addChild(cube_r68);
		setRotationAngle(cube_r68, 0.0F, 0.0F, 1.8588F);
		cube_r68.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r69 = new FlowerPart(this);
		cube_r69.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal21.addChild(cube_r69);
		setRotationAngle(cube_r69, 0.0F, 0.0F, 1.0036F);
		cube_r69.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r70 = new FlowerPart(this);
		cube_r70.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal21.addChild(cube_r70);
		setRotationAngle(cube_r70, 0.0F, -0.0873F, 0.5236F);
		cube_r70.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r71 = new FlowerPart(this);
		cube_r71.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal21.addChild(cube_r71);
		setRotationAngle(cube_r71, 0.0F, 0.0873F, 0.5236F);
	}

	private void init13() {
		cube_r71.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r72 = new FlowerPart(this);
		cube_r72.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal21.addChild(cube_r72);
		setRotationAngle(cube_r72, 0.0F, 0.0F, 0.5236F);
		cube_r72.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r72.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r73 = new FlowerPart(this);
		cube_r73.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal21.addChild(cube_r73);
		setRotationAngle(cube_r73, 0.0F, 0.0F, -0.1745F);
		cube_r73.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r74 = new FlowerPart(this);
		cube_r74.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal21.addChild(cube_r74);
		setRotationAngle(cube_r74, 0.0F, -0.2618F, -0.1745F);
		cube_r74.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r75 = new FlowerPart(this);
		cube_r75.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal21.addChild(cube_r75);
		setRotationAngle(cube_r75, 0.0F, 0.2618F, -0.1745F);
		cube_r75.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r76 = new FlowerPart(this);
		cube_r76.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal21.addChild(cube_r76);
		setRotationAngle(cube_r76, 0.0F, 0.0F, -0.1745F);
		cube_r76.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal22 = new FlowerPart(this);
		tepal22.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal22);
		setRotationAngle(tepal22, -3.1416F, 0.0F, -3.0543F);
		

		cube_r77 = new FlowerPart(this);
	}

	private void init14() {
		cube_r77.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal22.addChild(cube_r77);
		setRotationAngle(cube_r77, 0.0F, 0.0F, 0.2574F);
		cube_r77.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r78 = new FlowerPart(this);
		cube_r78.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal22.addChild(cube_r78);
		setRotationAngle(cube_r78, -0.0167F, 0.1453F, 0.7743F);
		cube_r78.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r79 = new FlowerPart(this);
		cube_r79.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal22.addChild(cube_r79);
		setRotationAngle(cube_r79, -0.0167F, -0.1339F, 0.7789F);
		cube_r79.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r80 = new FlowerPart(this);
		cube_r80.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal22.addChild(cube_r80);
		setRotationAngle(cube_r80, -0.0167F, -0.1339F, 0.7789F);
		cube_r80.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r81 = new FlowerPart(this);
		cube_r81.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal22.addChild(cube_r81);
		setRotationAngle(cube_r81, -0.0165F, 0.0057F, 0.7766F);
		cube_r81.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r81.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r81.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r82 = new FlowerPart(this);
		cube_r82.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal22.addChild(cube_r82);
		setRotationAngle(cube_r82, -0.0139F, -0.0105F, -0.2006F);
		cube_r82.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r83 = new FlowerPart(this);
		cube_r83.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal22.addChild(cube_r83);
	}

	private void init15() {
		setRotationAngle(cube_r83, 0.0015F, -0.0174F, -1.213F);
		cube_r83.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r84 = new FlowerPart(this);
		cube_r84.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal22.addChild(cube_r84);
		setRotationAngle(cube_r84, 0.0F, 0.1047F, -2.4696F);
		cube_r84.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r85 = new FlowerPart(this);
		cube_r85.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal22.addChild(cube_r85);
		setRotationAngle(cube_r85, 0.0F, -0.1047F, -2.4696F);
		cube_r85.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r86 = new FlowerPart(this);
		cube_r86.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal22.addChild(cube_r86);
		setRotationAngle(cube_r86, 0.0F, 0.0F, -2.4696F);
		cube_r86.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r86.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r86.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r87 = new FlowerPart(this);
		cube_r87.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal22.addChild(cube_r87);
		setRotationAngle(cube_r87, 0.0F, 0.0F, 2.8536F);
		cube_r87.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r88 = new FlowerPart(this);
		cube_r88.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal22.addChild(cube_r88);
		setRotationAngle(cube_r88, 0.0F, -0.0873F, 1.8588F);
		cube_r88.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r89 = new FlowerPart(this);
		cube_r89.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal22.addChild(cube_r89);
		setRotationAngle(cube_r89, 0.0F, 0.1309F, 1.8588F);
		cube_r89.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));
	}

	private void init16() {

		cube_r90 = new FlowerPart(this);
		cube_r90.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal22.addChild(cube_r90);
		setRotationAngle(cube_r90, 0.0F, 0.0F, 1.8588F);
		cube_r90.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r90.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r91 = new FlowerPart(this);
		cube_r91.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal22.addChild(cube_r91);
		setRotationAngle(cube_r91, 0.0F, 0.0F, 1.8588F);
		cube_r91.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r92 = new FlowerPart(this);
		cube_r92.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal22.addChild(cube_r92);
		setRotationAngle(cube_r92, 0.0F, 0.0F, 1.0036F);
		cube_r92.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r93 = new FlowerPart(this);
		cube_r93.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal22.addChild(cube_r93);
		setRotationAngle(cube_r93, 0.0F, -0.0873F, 0.5236F);
		cube_r93.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r94 = new FlowerPart(this);
		cube_r94.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal22.addChild(cube_r94);
		setRotationAngle(cube_r94, 0.0F, 0.0873F, 0.5236F);
		cube_r94.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r95 = new FlowerPart(this);
		cube_r95.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal22.addChild(cube_r95);
		setRotationAngle(cube_r95, 0.0F, 0.0F, 0.5236F);
		cube_r95.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r95.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r96 = new FlowerPart(this);
	}

	private void init17() {
		cube_r96.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal22.addChild(cube_r96);
		setRotationAngle(cube_r96, 0.0F, 0.0F, -0.1745F);
		cube_r96.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r97 = new FlowerPart(this);
		cube_r97.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal22.addChild(cube_r97);
		setRotationAngle(cube_r97, 0.0F, -0.2618F, -0.1745F);
		cube_r97.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r98 = new FlowerPart(this);
		cube_r98.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal22.addChild(cube_r98);
		setRotationAngle(cube_r98, 0.0F, 0.2618F, -0.1745F);
		cube_r98.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r99 = new FlowerPart(this);
		cube_r99.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal22.addChild(cube_r99);
		setRotationAngle(cube_r99, 0.0F, 0.0F, -0.1745F);
		cube_r99.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal23 = new FlowerPart(this);
		tepal23.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal23);
		setRotationAngle(tepal23, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r100 = new FlowerPart(this);
		cube_r100.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal23.addChild(cube_r100);
		setRotationAngle(cube_r100, 0.0F, 0.0F, 0.2574F);
		cube_r100.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r101 = new FlowerPart(this);
		cube_r101.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal23.addChild(cube_r101);
		setRotationAngle(cube_r101, -0.0167F, 0.1453F, 0.7743F);
		cube_r101.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init18() {

		cube_r102 = new FlowerPart(this);
		cube_r102.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal23.addChild(cube_r102);
		setRotationAngle(cube_r102, -0.0167F, -0.1339F, 0.7789F);
		cube_r102.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r103 = new FlowerPart(this);
		cube_r103.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal23.addChild(cube_r103);
		setRotationAngle(cube_r103, -0.0167F, -0.1339F, 0.7789F);
		cube_r103.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r104 = new FlowerPart(this);
		cube_r104.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal23.addChild(cube_r104);
		setRotationAngle(cube_r104, -0.0165F, 0.0057F, 0.7766F);
		cube_r104.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r104.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r104.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r105 = new FlowerPart(this);
		cube_r105.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal23.addChild(cube_r105);
		setRotationAngle(cube_r105, -0.0139F, -0.0105F, -0.2006F);
		cube_r105.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r106 = new FlowerPart(this);
		cube_r106.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal23.addChild(cube_r106);
		setRotationAngle(cube_r106, 0.0015F, -0.0174F, -1.213F);
		cube_r106.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r107 = new FlowerPart(this);
		cube_r107.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal23.addChild(cube_r107);
		setRotationAngle(cube_r107, 0.0F, 0.1047F, -2.4696F);
		cube_r107.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r108 = new FlowerPart(this);
	}

	private void init19() {
		cube_r108.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal23.addChild(cube_r108);
		setRotationAngle(cube_r108, 0.0F, -0.1047F, -2.4696F);
		cube_r108.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r109 = new FlowerPart(this);
		cube_r109.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal23.addChild(cube_r109);
		setRotationAngle(cube_r109, 0.0F, 0.0F, -2.4696F);
		cube_r109.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r109.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r109.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r110 = new FlowerPart(this);
		cube_r110.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal23.addChild(cube_r110);
		setRotationAngle(cube_r110, 0.0F, 0.0F, 2.8536F);
		cube_r110.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r111 = new FlowerPart(this);
		cube_r111.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal23.addChild(cube_r111);
		setRotationAngle(cube_r111, 0.0F, -0.0873F, 1.8588F);
		cube_r111.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r112 = new FlowerPart(this);
		cube_r112.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal23.addChild(cube_r112);
		setRotationAngle(cube_r112, 0.0F, 0.1309F, 1.8588F);
		cube_r112.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r113 = new FlowerPart(this);
		cube_r113.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal23.addChild(cube_r113);
		setRotationAngle(cube_r113, 0.0F, 0.0F, 1.8588F);
		cube_r113.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r113.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r114 = new FlowerPart(this);
		cube_r114.setRotationPoint(11.61F, 3.1F, 0.06F);
	}

	private void init20() {
		tepal23.addChild(cube_r114);
		setRotationAngle(cube_r114, 0.0F, 0.0F, 1.8588F);
		cube_r114.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r115 = new FlowerPart(this);
		cube_r115.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal23.addChild(cube_r115);
		setRotationAngle(cube_r115, 0.0F, 0.0F, 1.0036F);
		cube_r115.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r116 = new FlowerPart(this);
		cube_r116.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal23.addChild(cube_r116);
		setRotationAngle(cube_r116, 0.0F, -0.0873F, 0.5236F);
		cube_r116.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r117 = new FlowerPart(this);
		cube_r117.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal23.addChild(cube_r117);
		setRotationAngle(cube_r117, 0.0F, 0.0873F, 0.5236F);
		cube_r117.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r118 = new FlowerPart(this);
		cube_r118.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal23.addChild(cube_r118);
		setRotationAngle(cube_r118, 0.0F, 0.0F, 0.5236F);
		cube_r118.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r118.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r119 = new FlowerPart(this);
		cube_r119.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal23.addChild(cube_r119);
		setRotationAngle(cube_r119, 0.0F, 0.0F, -0.1745F);
		cube_r119.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r120 = new FlowerPart(this);
		cube_r120.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal23.addChild(cube_r120);
		setRotationAngle(cube_r120, 0.0F, -0.2618F, -0.1745F);
		cube_r120.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init21() {

		cube_r121 = new FlowerPart(this);
		cube_r121.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal23.addChild(cube_r121);
		setRotationAngle(cube_r121, 0.0F, 0.2618F, -0.1745F);
		cube_r121.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r122 = new FlowerPart(this);
		cube_r122.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal23.addChild(cube_r122);
		setRotationAngle(cube_r122, 0.0F, 0.0F, -0.1745F);
		cube_r122.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal24 = new FlowerPart(this);
		tepal24.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth4.addChild(tepal24);
		setRotationAngle(tepal24, 0.5348F, -0.9507F, -0.891F);
		

		cube_r123 = new FlowerPart(this);
		cube_r123.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal24.addChild(cube_r123);
		setRotationAngle(cube_r123, 0.0F, 0.0F, 0.2574F);
		cube_r123.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r124 = new FlowerPart(this);
		cube_r124.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal24.addChild(cube_r124);
		setRotationAngle(cube_r124, -0.0167F, 0.1453F, 0.7743F);
		cube_r124.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r125 = new FlowerPart(this);
		cube_r125.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal24.addChild(cube_r125);
		setRotationAngle(cube_r125, -0.0167F, -0.1339F, 0.7789F);
		cube_r125.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r126 = new FlowerPart(this);
		cube_r126.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal24.addChild(cube_r126);
	}

	private void init22() {
		setRotationAngle(cube_r126, -0.0167F, -0.1339F, 0.7789F);
		cube_r126.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r127 = new FlowerPart(this);
		cube_r127.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal24.addChild(cube_r127);
		setRotationAngle(cube_r127, -0.0165F, 0.0057F, 0.7766F);
		cube_r127.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r127.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r127.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r128 = new FlowerPart(this);
		cube_r128.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal24.addChild(cube_r128);
		setRotationAngle(cube_r128, -0.0139F, -0.0105F, -0.2006F);
		cube_r128.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r129 = new FlowerPart(this);
		cube_r129.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal24.addChild(cube_r129);
		setRotationAngle(cube_r129, 0.0015F, -0.0174F, -1.213F);
		cube_r129.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r130 = new FlowerPart(this);
		cube_r130.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal24.addChild(cube_r130);
		setRotationAngle(cube_r130, 0.0F, 0.1047F, -2.4696F);
		cube_r130.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r131 = new FlowerPart(this);
		cube_r131.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal24.addChild(cube_r131);
		setRotationAngle(cube_r131, 0.0F, -0.1047F, -2.4696F);
		cube_r131.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r132 = new FlowerPart(this);
		cube_r132.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal24.addChild(cube_r132);
		setRotationAngle(cube_r132, 0.0F, 0.0F, -2.4696F);
		cube_r132.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
	}

	private void init23() {
		cube_r132.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r132.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r133 = new FlowerPart(this);
		cube_r133.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal24.addChild(cube_r133);
		setRotationAngle(cube_r133, 0.0F, 0.0F, 2.8536F);
		cube_r133.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r134 = new FlowerPart(this);
		cube_r134.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal24.addChild(cube_r134);
		setRotationAngle(cube_r134, 0.0F, -0.0873F, 1.8588F);
		cube_r134.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r135 = new FlowerPart(this);
		cube_r135.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal24.addChild(cube_r135);
		setRotationAngle(cube_r135, 0.0F, 0.1309F, 1.8588F);
		cube_r135.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r136 = new FlowerPart(this);
		cube_r136.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal24.addChild(cube_r136);
		setRotationAngle(cube_r136, 0.0F, 0.0F, 1.8588F);
		cube_r136.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r136.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r137 = new FlowerPart(this);
		cube_r137.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal24.addChild(cube_r137);
		setRotationAngle(cube_r137, 0.0F, 0.0F, 1.8588F);
		cube_r137.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r138 = new FlowerPart(this);
		cube_r138.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal24.addChild(cube_r138);
		setRotationAngle(cube_r138, 0.0F, 0.0F, 1.0036F);
		cube_r138.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r139 = new FlowerPart(this);
	}

	private void init24() {
		cube_r139.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal24.addChild(cube_r139);
		setRotationAngle(cube_r139, 0.0F, -0.0873F, 0.5236F);
		cube_r139.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r140 = new FlowerPart(this);
		cube_r140.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal24.addChild(cube_r140);
		setRotationAngle(cube_r140, 0.0F, 0.0873F, 0.5236F);
		cube_r140.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r141 = new FlowerPart(this);
		cube_r141.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal24.addChild(cube_r141);
		setRotationAngle(cube_r141, 0.0F, 0.0F, 0.5236F);
		cube_r141.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r141.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r142 = new FlowerPart(this);
		cube_r142.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal24.addChild(cube_r142);
		setRotationAngle(cube_r142, 0.0F, 0.0F, -0.1745F);
		cube_r142.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r143 = new FlowerPart(this);
		cube_r143.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal24.addChild(cube_r143);
		setRotationAngle(cube_r143, 0.0F, -0.2618F, -0.1745F);
		cube_r143.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r144 = new FlowerPart(this);
		cube_r144.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal24.addChild(cube_r144);
		setRotationAngle(cube_r144, 0.0F, 0.2618F, -0.1745F);
		cube_r144.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r145 = new FlowerPart(this);
		cube_r145.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal24.addChild(cube_r145);
		setRotationAngle(cube_r145, 0.0F, 0.0F, -0.1745F);
	}

	private void init25() {
		cube_r145.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal19 = new FlowerPart(this);
		stemal19.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth4.addChild(stemal19);
		setRotationAngle(stemal19, -0.4931F, -0.438F, -0.0571F);
		stemal19.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r146 = new FlowerPart(this);
		cube_r146.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal19.addChild(cube_r146);
		setRotationAngle(cube_r146, 0.0F, 0.0F, 0.8814F);
		cube_r146.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r147 = new FlowerPart(this);
		cube_r147.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal19.addChild(cube_r147);
		setRotationAngle(cube_r147, 0.0F, 0.0F, 0.2618F);
		cube_r147.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r148 = new FlowerPart(this);
		cube_r148.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal19.addChild(cube_r148);
		setRotationAngle(cube_r148, 0.0F, 0.0F, 0.8814F);
		cube_r148.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r149 = new FlowerPart(this);
		cube_r149.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal19.addChild(cube_r149);
		setRotationAngle(cube_r149, 0.0F, 0.0F, 0.5498F);
		cube_r149.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r150 = new FlowerPart(this);
		cube_r150.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal19.addChild(cube_r150);
		setRotationAngle(cube_r150, 0.0F, 0.0F, 0.8814F);
		cube_r150.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal20 = new FlowerPart(this);
		stemal20.setRotationPoint(0.0F, -1.0F, 0.0F);
	}

	private void init26() {
		perianth4.addChild(stemal20);
		setRotationAngle(stemal20, -0.1017F, 0.0303F, 0.0497F);
		stemal20.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r151 = new FlowerPart(this);
		cube_r151.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal20.addChild(cube_r151);
		setRotationAngle(cube_r151, 0.0F, 0.0F, 0.8814F);
		cube_r151.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r152 = new FlowerPart(this);
		cube_r152.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal20.addChild(cube_r152);
		setRotationAngle(cube_r152, 0.0F, 0.0F, 0.2618F);
		cube_r152.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r153 = new FlowerPart(this);
		cube_r153.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal20.addChild(cube_r153);
		setRotationAngle(cube_r153, 0.0F, 0.0F, 0.8814F);
		cube_r153.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r154 = new FlowerPart(this);
		cube_r154.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal20.addChild(cube_r154);
		setRotationAngle(cube_r154, 0.0F, 0.0F, 0.5498F);
		cube_r154.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r155 = new FlowerPart(this);
		cube_r155.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal20.addChild(cube_r155);
		setRotationAngle(cube_r155, 0.0F, 0.0F, 0.8814F);
		cube_r155.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal21 = new FlowerPart(this);
		stemal21.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth4.addChild(stemal21);
		setRotationAngle(stemal21, 0.1582F, 0.2804F, -0.7053F);
		stemal21.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r156 = new FlowerPart(this);
	}

	private void init27() {
		cube_r156.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal21.addChild(cube_r156);
		setRotationAngle(cube_r156, 0.0F, 0.0F, 0.8814F);
		cube_r156.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r157 = new FlowerPart(this);
		cube_r157.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal21.addChild(cube_r157);
		setRotationAngle(cube_r157, 0.0F, 0.0F, 0.2618F);
		cube_r157.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r158 = new FlowerPart(this);
		cube_r158.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal21.addChild(cube_r158);
		setRotationAngle(cube_r158, 0.0F, 0.0F, 0.8814F);
		cube_r158.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r159 = new FlowerPart(this);
		cube_r159.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal21.addChild(cube_r159);
		setRotationAngle(cube_r159, 0.0F, 0.0F, 0.5498F);
		cube_r159.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r160 = new FlowerPart(this);
		cube_r160.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal21.addChild(cube_r160);
		setRotationAngle(cube_r160, 0.0F, 0.0F, 0.8814F);
		cube_r160.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal22 = new FlowerPart(this);
		stemal22.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth4.addChild(stemal22);
		setRotationAngle(stemal22, -0.2949F, -0.1812F, -0.7237F);
		stemal22.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r161 = new FlowerPart(this);
		cube_r161.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal22.addChild(cube_r161);
		setRotationAngle(cube_r161, 0.0F, 0.0F, 0.8814F);
		cube_r161.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));
	}

	private void init28() {

		cube_r162 = new FlowerPart(this);
		cube_r162.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal22.addChild(cube_r162);
		setRotationAngle(cube_r162, 0.0F, 0.0F, 0.2618F);
		cube_r162.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r163 = new FlowerPart(this);
		cube_r163.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal22.addChild(cube_r163);
		setRotationAngle(cube_r163, 0.0F, 0.0F, 0.8814F);
		cube_r163.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r164 = new FlowerPart(this);
		cube_r164.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal22.addChild(cube_r164);
		setRotationAngle(cube_r164, 0.0F, 0.0F, 0.5498F);
		cube_r164.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r165 = new FlowerPart(this);
		cube_r165.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal22.addChild(cube_r165);
		setRotationAngle(cube_r165, 0.0F, 0.0F, 0.8814F);
		cube_r165.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal23 = new FlowerPart(this);
		stemal23.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth4.addChild(stemal23);
		setRotationAngle(stemal23, 0.3149F, 0.1775F, -0.2915F);
		stemal23.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r166 = new FlowerPart(this);
		cube_r166.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal23.addChild(cube_r166);
		setRotationAngle(cube_r166, 0.0F, 0.0F, 0.8814F);
		cube_r166.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r167 = new FlowerPart(this);
		cube_r167.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal23.addChild(cube_r167);
	}

	private void init29() {
		setRotationAngle(cube_r167, 0.0F, 0.0F, 0.2618F);
		cube_r167.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r168 = new FlowerPart(this);
		cube_r168.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal23.addChild(cube_r168);
		setRotationAngle(cube_r168, 0.0F, 0.0F, 0.8814F);
		cube_r168.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r169 = new FlowerPart(this);
		cube_r169.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal23.addChild(cube_r169);
		setRotationAngle(cube_r169, 0.0F, 0.0F, 0.5498F);
		cube_r169.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r170 = new FlowerPart(this);
		cube_r170.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal23.addChild(cube_r170);
		setRotationAngle(cube_r170, 0.0F, 0.0F, 0.8814F);
		cube_r170.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal24 = new FlowerPart(this);
		stemal24.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth4.addChild(stemal24);
		setRotationAngle(stemal24, 0.0F, 0.0F, -0.9163F);
		stemal24.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r171 = new FlowerPart(this);
		cube_r171.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal24.addChild(cube_r171);
		setRotationAngle(cube_r171, 0.0F, 0.0F, 0.8814F);
		cube_r171.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r172 = new FlowerPart(this);
		cube_r172.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal24.addChild(cube_r172);
		setRotationAngle(cube_r172, 0.0F, 0.0F, 0.2618F);
		cube_r172.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r173 = new FlowerPart(this);
	}

	private void init30() {
		cube_r173.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal24.addChild(cube_r173);
		setRotationAngle(cube_r173, 0.0F, 0.0F, 0.8814F);
		cube_r173.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r174 = new FlowerPart(this);
		cube_r174.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal24.addChild(cube_r174);
		setRotationAngle(cube_r174, 0.0F, 0.0F, 0.5498F);
		cube_r174.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r175 = new FlowerPart(this);
		cube_r175.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal24.addChild(cube_r175);
		setRotationAngle(cube_r175, 0.0F, 0.0F, 0.8814F);
		cube_r175.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		perianth5 = new FlowerPart(this);
		perianth5.setRotationPoint(-11.26F, -2.17F, 4.755F);
		half1.addChild(perianth5);
		setRotationAngle(perianth5, -2.1595F, 0.9246F, -1.9402F);
		

		tepal25 = new FlowerPart(this);
		tepal25.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal25);
		setRotationAngle(tepal25, -0.0019F, 0.1309F, -0.6547F);
		

		cube_r176 = new FlowerPart(this);
		cube_r176.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal25.addChild(cube_r176);
		setRotationAngle(cube_r176, 0.0F, 0.0F, 0.2574F);
		cube_r176.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r177 = new FlowerPart(this);
		cube_r177.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal25.addChild(cube_r177);
		setRotationAngle(cube_r177, -0.0167F, 0.1453F, 0.7743F);
		cube_r177.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init31() {

		cube_r178 = new FlowerPart(this);
		cube_r178.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal25.addChild(cube_r178);
		setRotationAngle(cube_r178, -0.0167F, -0.1339F, 0.7789F);
		cube_r178.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r179 = new FlowerPart(this);
		cube_r179.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal25.addChild(cube_r179);
		setRotationAngle(cube_r179, -0.0167F, -0.1339F, 0.7789F);
		cube_r179.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r180 = new FlowerPart(this);
		cube_r180.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal25.addChild(cube_r180);
		setRotationAngle(cube_r180, -0.0165F, 0.0057F, 0.7766F);
		cube_r180.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r180.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r180.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r181 = new FlowerPart(this);
		cube_r181.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal25.addChild(cube_r181);
		setRotationAngle(cube_r181, -0.0139F, -0.0105F, -0.2006F);
		cube_r181.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r182 = new FlowerPart(this);
		cube_r182.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal25.addChild(cube_r182);
		setRotationAngle(cube_r182, 0.0015F, -0.0174F, -1.213F);
		cube_r182.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r183 = new FlowerPart(this);
		cube_r183.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal25.addChild(cube_r183);
		setRotationAngle(cube_r183, 0.0F, 0.1047F, -2.4696F);
		cube_r183.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r184 = new FlowerPart(this);
	}

	private void init32() {
		cube_r184.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal25.addChild(cube_r184);
		setRotationAngle(cube_r184, 0.0F, -0.1047F, -2.4696F);
		cube_r184.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r185 = new FlowerPart(this);
		cube_r185.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal25.addChild(cube_r185);
		setRotationAngle(cube_r185, 0.0F, 0.0F, -2.4696F);
		cube_r185.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r185.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r185.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r186 = new FlowerPart(this);
		cube_r186.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal25.addChild(cube_r186);
		setRotationAngle(cube_r186, 0.0F, 0.0F, 2.8536F);
		cube_r186.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r187 = new FlowerPart(this);
		cube_r187.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal25.addChild(cube_r187);
		setRotationAngle(cube_r187, 0.0F, -0.0873F, 1.8588F);
		cube_r187.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r188 = new FlowerPart(this);
		cube_r188.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal25.addChild(cube_r188);
		setRotationAngle(cube_r188, 0.0F, 0.1309F, 1.8588F);
		cube_r188.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r189 = new FlowerPart(this);
		cube_r189.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal25.addChild(cube_r189);
		setRotationAngle(cube_r189, 0.0F, 0.0F, 1.8588F);
		cube_r189.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r189.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r190 = new FlowerPart(this);
		cube_r190.setRotationPoint(11.61F, 3.1F, 0.06F);
	}

	private void init33() {
		tepal25.addChild(cube_r190);
		setRotationAngle(cube_r190, 0.0F, 0.0F, 1.8588F);
		cube_r190.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r191 = new FlowerPart(this);
		cube_r191.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal25.addChild(cube_r191);
		setRotationAngle(cube_r191, 0.0F, 0.0F, 1.0036F);
		cube_r191.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r192 = new FlowerPart(this);
		cube_r192.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal25.addChild(cube_r192);
		setRotationAngle(cube_r192, 0.0F, -0.0873F, 0.5236F);
		cube_r192.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r193 = new FlowerPart(this);
		cube_r193.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal25.addChild(cube_r193);
		setRotationAngle(cube_r193, 0.0F, 0.0873F, 0.5236F);
		cube_r193.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r194 = new FlowerPart(this);
		cube_r194.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal25.addChild(cube_r194);
		setRotationAngle(cube_r194, 0.0F, 0.0F, 0.5236F);
		cube_r194.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r194.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r195 = new FlowerPart(this);
		cube_r195.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal25.addChild(cube_r195);
		setRotationAngle(cube_r195, 0.0F, 0.0F, -0.1745F);
		cube_r195.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r196 = new FlowerPart(this);
		cube_r196.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal25.addChild(cube_r196);
		setRotationAngle(cube_r196, 0.0F, -0.2618F, -0.1745F);
		cube_r196.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init34() {

		cube_r197 = new FlowerPart(this);
		cube_r197.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal25.addChild(cube_r197);
		setRotationAngle(cube_r197, 0.0F, 0.2618F, -0.1745F);
		cube_r197.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r198 = new FlowerPart(this);
		cube_r198.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal25.addChild(cube_r198);
		setRotationAngle(cube_r198, 0.0F, 0.0F, -0.1745F);
		cube_r198.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal26 = new FlowerPart(this);
		tepal26.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal26);
		setRotationAngle(tepal26, -0.6063F, 1.0242F, -0.9766F);
		

		cube_r199 = new FlowerPart(this);
		cube_r199.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal26.addChild(cube_r199);
		setRotationAngle(cube_r199, 0.0F, 0.0F, 0.2574F);
		cube_r199.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r200 = new FlowerPart(this);
		cube_r200.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal26.addChild(cube_r200);
		setRotationAngle(cube_r200, -0.0167F, 0.1453F, 0.7743F);
		cube_r200.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r201 = new FlowerPart(this);
		cube_r201.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal26.addChild(cube_r201);
		setRotationAngle(cube_r201, -0.0167F, -0.1339F, 0.7789F);
		cube_r201.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r202 = new FlowerPart(this);
		cube_r202.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal26.addChild(cube_r202);
	}

	private void init35() {
		setRotationAngle(cube_r202, -0.0167F, -0.1339F, 0.7789F);
		cube_r202.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r203 = new FlowerPart(this);
		cube_r203.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal26.addChild(cube_r203);
		setRotationAngle(cube_r203, -0.0165F, 0.0057F, 0.7766F);
		cube_r203.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r203.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r203.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r204 = new FlowerPart(this);
		cube_r204.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal26.addChild(cube_r204);
		setRotationAngle(cube_r204, -0.0139F, -0.0105F, -0.2006F);
		cube_r204.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r205 = new FlowerPart(this);
		cube_r205.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal26.addChild(cube_r205);
		setRotationAngle(cube_r205, 0.0015F, -0.0174F, -1.213F);
		cube_r205.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r206 = new FlowerPart(this);
		cube_r206.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal26.addChild(cube_r206);
		setRotationAngle(cube_r206, 0.0F, 0.1047F, -2.4696F);
		cube_r206.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r207 = new FlowerPart(this);
		cube_r207.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal26.addChild(cube_r207);
		setRotationAngle(cube_r207, 0.0F, -0.1047F, -2.4696F);
		cube_r207.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r208 = new FlowerPart(this);
		cube_r208.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal26.addChild(cube_r208);
		setRotationAngle(cube_r208, 0.0F, 0.0F, -2.4696F);
		cube_r208.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
	}

	private void init36() {
		cube_r208.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r208.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r209 = new FlowerPart(this);
		cube_r209.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal26.addChild(cube_r209);
		setRotationAngle(cube_r209, 0.0F, 0.0F, 2.8536F);
		cube_r209.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r210 = new FlowerPart(this);
		cube_r210.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal26.addChild(cube_r210);
		setRotationAngle(cube_r210, 0.0F, -0.0873F, 1.8588F);
		cube_r210.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r211 = new FlowerPart(this);
		cube_r211.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal26.addChild(cube_r211);
		setRotationAngle(cube_r211, 0.0F, 0.1309F, 1.8588F);
		cube_r211.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r212 = new FlowerPart(this);
		cube_r212.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal26.addChild(cube_r212);
		setRotationAngle(cube_r212, 0.0F, 0.0F, 1.8588F);
		cube_r212.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r212.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r213 = new FlowerPart(this);
		cube_r213.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal26.addChild(cube_r213);
		setRotationAngle(cube_r213, 0.0F, 0.0F, 1.8588F);
		cube_r213.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r214 = new FlowerPart(this);
		cube_r214.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal26.addChild(cube_r214);
		setRotationAngle(cube_r214, 0.0F, 0.0F, 1.0036F);
		cube_r214.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r215 = new FlowerPart(this);
	}

	private void init37() {
		cube_r215.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal26.addChild(cube_r215);
		setRotationAngle(cube_r215, 0.0F, -0.0873F, 0.5236F);
		cube_r215.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r216 = new FlowerPart(this);
		cube_r216.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal26.addChild(cube_r216);
		setRotationAngle(cube_r216, 0.0F, 0.0873F, 0.5236F);
		cube_r216.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r217 = new FlowerPart(this);
		cube_r217.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal26.addChild(cube_r217);
		setRotationAngle(cube_r217, 0.0F, 0.0F, 0.5236F);
		cube_r217.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r217.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r218 = new FlowerPart(this);
		cube_r218.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal26.addChild(cube_r218);
		setRotationAngle(cube_r218, 0.0F, 0.0F, -0.1745F);
		cube_r218.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r219 = new FlowerPart(this);
		cube_r219.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal26.addChild(cube_r219);
		setRotationAngle(cube_r219, 0.0F, -0.2618F, -0.1745F);
		cube_r219.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r220 = new FlowerPart(this);
		cube_r220.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal26.addChild(cube_r220);
		setRotationAngle(cube_r220, 0.0F, 0.2618F, -0.1745F);
		cube_r220.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r221 = new FlowerPart(this);
		cube_r221.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal26.addChild(cube_r221);
		setRotationAngle(cube_r221, 0.0F, 0.0F, -0.1745F);
	}

	private void init38() {
		cube_r221.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal27 = new FlowerPart(this);
		tepal27.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal27);
		setRotationAngle(tepal27, -2.6068F, 0.9507F, -2.7742F);
		

		cube_r222 = new FlowerPart(this);
		cube_r222.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal27.addChild(cube_r222);
		setRotationAngle(cube_r222, 0.0F, 0.0F, 0.2574F);
		cube_r222.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r223 = new FlowerPart(this);
		cube_r223.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal27.addChild(cube_r223);
		setRotationAngle(cube_r223, -0.0167F, 0.1453F, 0.7743F);
		cube_r223.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r224 = new FlowerPart(this);
		cube_r224.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal27.addChild(cube_r224);
		setRotationAngle(cube_r224, -0.0167F, -0.1339F, 0.7789F);
		cube_r224.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r225 = new FlowerPart(this);
		cube_r225.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal27.addChild(cube_r225);
		setRotationAngle(cube_r225, -0.0167F, -0.1339F, 0.7789F);
		cube_r225.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r226 = new FlowerPart(this);
		cube_r226.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal27.addChild(cube_r226);
		setRotationAngle(cube_r226, -0.0165F, 0.0057F, 0.7766F);
		cube_r226.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r226.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r226.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r227 = new FlowerPart(this);
	}

	private void init39() {
		cube_r227.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal27.addChild(cube_r227);
		setRotationAngle(cube_r227, -0.0139F, -0.0105F, -0.2006F);
		cube_r227.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r228 = new FlowerPart(this);
		cube_r228.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal27.addChild(cube_r228);
		setRotationAngle(cube_r228, 0.0015F, -0.0174F, -1.213F);
		cube_r228.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r229 = new FlowerPart(this);
		cube_r229.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal27.addChild(cube_r229);
		setRotationAngle(cube_r229, 0.0F, 0.1047F, -2.4696F);
		cube_r229.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r230 = new FlowerPart(this);
		cube_r230.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal27.addChild(cube_r230);
		setRotationAngle(cube_r230, 0.0F, -0.1047F, -2.4696F);
		cube_r230.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r231 = new FlowerPart(this);
		cube_r231.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal27.addChild(cube_r231);
		setRotationAngle(cube_r231, 0.0F, 0.0F, -2.4696F);
		cube_r231.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r231.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r231.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r232 = new FlowerPart(this);
		cube_r232.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal27.addChild(cube_r232);
		setRotationAngle(cube_r232, 0.0F, 0.0F, 2.8536F);
		cube_r232.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r233 = new FlowerPart(this);
		cube_r233.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal27.addChild(cube_r233);
	}

	private void init40() {
		setRotationAngle(cube_r233, 0.0F, -0.0873F, 1.8588F);
		cube_r233.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r234 = new FlowerPart(this);
		cube_r234.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal27.addChild(cube_r234);
		setRotationAngle(cube_r234, 0.0F, 0.1309F, 1.8588F);
		cube_r234.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r235 = new FlowerPart(this);
		cube_r235.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal27.addChild(cube_r235);
		setRotationAngle(cube_r235, 0.0F, 0.0F, 1.8588F);
		cube_r235.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r235.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r236 = new FlowerPart(this);
		cube_r236.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal27.addChild(cube_r236);
		setRotationAngle(cube_r236, 0.0F, 0.0F, 1.8588F);
		cube_r236.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r237 = new FlowerPart(this);
		cube_r237.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal27.addChild(cube_r237);
		setRotationAngle(cube_r237, 0.0F, 0.0F, 1.0036F);
		cube_r237.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r238 = new FlowerPart(this);
		cube_r238.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal27.addChild(cube_r238);
		setRotationAngle(cube_r238, 0.0F, -0.0873F, 0.5236F);
		cube_r238.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r239 = new FlowerPart(this);
		cube_r239.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal27.addChild(cube_r239);
		setRotationAngle(cube_r239, 0.0F, 0.0873F, 0.5236F);
		cube_r239.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r240 = new FlowerPart(this);
	}

	private void init41() {
		cube_r240.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal27.addChild(cube_r240);
		setRotationAngle(cube_r240, 0.0F, 0.0F, 0.5236F);
		cube_r240.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r240.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r241 = new FlowerPart(this);
		cube_r241.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal27.addChild(cube_r241);
		setRotationAngle(cube_r241, 0.0F, 0.0F, -0.1745F);
		cube_r241.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r242 = new FlowerPart(this);
		cube_r242.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal27.addChild(cube_r242);
		setRotationAngle(cube_r242, 0.0F, -0.2618F, -0.1745F);
		cube_r242.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r243 = new FlowerPart(this);
		cube_r243.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal27.addChild(cube_r243);
		setRotationAngle(cube_r243, 0.0F, 0.2618F, -0.1745F);
		cube_r243.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r244 = new FlowerPart(this);
		cube_r244.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal27.addChild(cube_r244);
		setRotationAngle(cube_r244, 0.0F, 0.0F, -0.1745F);
		cube_r244.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal28 = new FlowerPart(this);
		tepal28.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal28);
		setRotationAngle(tepal28, -3.1416F, 0.0F, -3.0543F);
		

		cube_r245 = new FlowerPart(this);
		cube_r245.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal28.addChild(cube_r245);
		setRotationAngle(cube_r245, 0.0F, 0.0F, 0.2574F);
	}

	private void init42() {
		cube_r245.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r246 = new FlowerPart(this);
		cube_r246.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal28.addChild(cube_r246);
		setRotationAngle(cube_r246, -0.0167F, 0.1453F, 0.7743F);
		cube_r246.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r247 = new FlowerPart(this);
		cube_r247.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal28.addChild(cube_r247);
		setRotationAngle(cube_r247, -0.0167F, -0.1339F, 0.7789F);
		cube_r247.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r248 = new FlowerPart(this);
		cube_r248.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal28.addChild(cube_r248);
		setRotationAngle(cube_r248, -0.0167F, -0.1339F, 0.7789F);
		cube_r248.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r249 = new FlowerPart(this);
		cube_r249.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal28.addChild(cube_r249);
		setRotationAngle(cube_r249, -0.0165F, 0.0057F, 0.7766F);
		cube_r249.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r249.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r249.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r250 = new FlowerPart(this);
		cube_r250.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal28.addChild(cube_r250);
		setRotationAngle(cube_r250, -0.0139F, -0.0105F, -0.2006F);
		cube_r250.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r251 = new FlowerPart(this);
		cube_r251.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal28.addChild(cube_r251);
		setRotationAngle(cube_r251, 0.0015F, -0.0174F, -1.213F);
		cube_r251.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r252 = new FlowerPart(this);
	}

	private void init43() {
		cube_r252.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal28.addChild(cube_r252);
		setRotationAngle(cube_r252, 0.0F, 0.1047F, -2.4696F);
		cube_r252.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r253 = new FlowerPart(this);
		cube_r253.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal28.addChild(cube_r253);
		setRotationAngle(cube_r253, 0.0F, -0.1047F, -2.4696F);
		cube_r253.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r254 = new FlowerPart(this);
		cube_r254.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal28.addChild(cube_r254);
		setRotationAngle(cube_r254, 0.0F, 0.0F, -2.4696F);
		cube_r254.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r254.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r254.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r255 = new FlowerPart(this);
		cube_r255.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal28.addChild(cube_r255);
		setRotationAngle(cube_r255, 0.0F, 0.0F, 2.8536F);
		cube_r255.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r256 = new FlowerPart(this);
		cube_r256.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal28.addChild(cube_r256);
		setRotationAngle(cube_r256, 0.0F, -0.0873F, 1.8588F);
		cube_r256.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r257 = new FlowerPart(this);
		cube_r257.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal28.addChild(cube_r257);
		setRotationAngle(cube_r257, 0.0F, 0.1309F, 1.8588F);
		cube_r257.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r258 = new FlowerPart(this);
		cube_r258.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal28.addChild(cube_r258);
	}

	private void init44() {
		setRotationAngle(cube_r258, 0.0F, 0.0F, 1.8588F);
		cube_r258.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r258.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r259 = new FlowerPart(this);
		cube_r259.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal28.addChild(cube_r259);
		setRotationAngle(cube_r259, 0.0F, 0.0F, 1.8588F);
		cube_r259.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r260 = new FlowerPart(this);
		cube_r260.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal28.addChild(cube_r260);
		setRotationAngle(cube_r260, 0.0F, 0.0F, 1.0036F);
		cube_r260.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r261 = new FlowerPart(this);
		cube_r261.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal28.addChild(cube_r261);
		setRotationAngle(cube_r261, 0.0F, -0.0873F, 0.5236F);
		cube_r261.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r262 = new FlowerPart(this);
		cube_r262.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal28.addChild(cube_r262);
		setRotationAngle(cube_r262, 0.0F, 0.0873F, 0.5236F);
		cube_r262.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r263 = new FlowerPart(this);
		cube_r263.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal28.addChild(cube_r263);
		setRotationAngle(cube_r263, 0.0F, 0.0F, 0.5236F);
		cube_r263.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r263.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r264 = new FlowerPart(this);
		cube_r264.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal28.addChild(cube_r264);
		setRotationAngle(cube_r264, 0.0F, 0.0F, -0.1745F);
		cube_r264.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));
	}

	private void init45() {

		cube_r265 = new FlowerPart(this);
		cube_r265.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal28.addChild(cube_r265);
		setRotationAngle(cube_r265, 0.0F, -0.2618F, -0.1745F);
		cube_r265.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r266 = new FlowerPart(this);
		cube_r266.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal28.addChild(cube_r266);
		setRotationAngle(cube_r266, 0.0F, 0.2618F, -0.1745F);
		cube_r266.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r267 = new FlowerPart(this);
		cube_r267.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal28.addChild(cube_r267);
		setRotationAngle(cube_r267, 0.0F, 0.0F, -0.1745F);
		cube_r267.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal29 = new FlowerPart(this);
		tepal29.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal29);
		setRotationAngle(tepal29, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r268 = new FlowerPart(this);
		cube_r268.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal29.addChild(cube_r268);
		setRotationAngle(cube_r268, 0.0F, 0.0F, 0.2574F);
		cube_r268.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r269 = new FlowerPart(this);
		cube_r269.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal29.addChild(cube_r269);
		setRotationAngle(cube_r269, -0.0167F, 0.1453F, 0.7743F);
		cube_r269.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r270 = new FlowerPart(this);
		cube_r270.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal29.addChild(cube_r270);
	}

	private void init46() {
		setRotationAngle(cube_r270, -0.0167F, -0.1339F, 0.7789F);
		cube_r270.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r271 = new FlowerPart(this);
		cube_r271.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal29.addChild(cube_r271);
		setRotationAngle(cube_r271, -0.0167F, -0.1339F, 0.7789F);
		cube_r271.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r272 = new FlowerPart(this);
		cube_r272.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal29.addChild(cube_r272);
		setRotationAngle(cube_r272, -0.0165F, 0.0057F, 0.7766F);
		cube_r272.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r272.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r272.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r273 = new FlowerPart(this);
		cube_r273.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal29.addChild(cube_r273);
		setRotationAngle(cube_r273, -0.0139F, -0.0105F, -0.2006F);
		cube_r273.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r274 = new FlowerPart(this);
		cube_r274.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal29.addChild(cube_r274);
		setRotationAngle(cube_r274, 0.0015F, -0.0174F, -1.213F);
		cube_r274.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r275 = new FlowerPart(this);
		cube_r275.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal29.addChild(cube_r275);
		setRotationAngle(cube_r275, 0.0F, 0.1047F, -2.4696F);
		cube_r275.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r276 = new FlowerPart(this);
		cube_r276.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal29.addChild(cube_r276);
		setRotationAngle(cube_r276, 0.0F, -0.1047F, -2.4696F);
		cube_r276.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init47() {

		cube_r277 = new FlowerPart(this);
		cube_r277.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal29.addChild(cube_r277);
		setRotationAngle(cube_r277, 0.0F, 0.0F, -2.4696F);
		cube_r277.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r277.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r277.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r278 = new FlowerPart(this);
		cube_r278.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal29.addChild(cube_r278);
		setRotationAngle(cube_r278, 0.0F, 0.0F, 2.8536F);
		cube_r278.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r279 = new FlowerPart(this);
		cube_r279.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal29.addChild(cube_r279);
		setRotationAngle(cube_r279, 0.0F, -0.0873F, 1.8588F);
		cube_r279.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r280 = new FlowerPart(this);
		cube_r280.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal29.addChild(cube_r280);
		setRotationAngle(cube_r280, 0.0F, 0.1309F, 1.8588F);
		cube_r280.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r281 = new FlowerPart(this);
		cube_r281.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal29.addChild(cube_r281);
		setRotationAngle(cube_r281, 0.0F, 0.0F, 1.8588F);
		cube_r281.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r281.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r282 = new FlowerPart(this);
		cube_r282.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal29.addChild(cube_r282);
		setRotationAngle(cube_r282, 0.0F, 0.0F, 1.8588F);
		cube_r282.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r283 = new FlowerPart(this);
	}

	private void init48() {
		cube_r283.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal29.addChild(cube_r283);
		setRotationAngle(cube_r283, 0.0F, 0.0F, 1.0036F);
		cube_r283.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r284 = new FlowerPart(this);
		cube_r284.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal29.addChild(cube_r284);
		setRotationAngle(cube_r284, 0.0F, -0.0873F, 0.5236F);
		cube_r284.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r285 = new FlowerPart(this);
		cube_r285.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal29.addChild(cube_r285);
		setRotationAngle(cube_r285, 0.0F, 0.0873F, 0.5236F);
		cube_r285.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r286 = new FlowerPart(this);
		cube_r286.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal29.addChild(cube_r286);
		setRotationAngle(cube_r286, 0.0F, 0.0F, 0.5236F);
		cube_r286.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r286.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r287 = new FlowerPart(this);
		cube_r287.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal29.addChild(cube_r287);
		setRotationAngle(cube_r287, 0.0F, 0.0F, -0.1745F);
		cube_r287.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r288 = new FlowerPart(this);
		cube_r288.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal29.addChild(cube_r288);
		setRotationAngle(cube_r288, 0.0F, -0.2618F, -0.1745F);
		cube_r288.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r289 = new FlowerPart(this);
		cube_r289.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal29.addChild(cube_r289);
		setRotationAngle(cube_r289, 0.0F, 0.2618F, -0.1745F);
	}

	private void init49() {
		cube_r289.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r290 = new FlowerPart(this);
		cube_r290.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal29.addChild(cube_r290);
		setRotationAngle(cube_r290, 0.0F, 0.0F, -0.1745F);
		cube_r290.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal30 = new FlowerPart(this);
		tepal30.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth5.addChild(tepal30);
		setRotationAngle(tepal30, 0.5348F, -0.9507F, -0.891F);
		

		cube_r291 = new FlowerPart(this);
		cube_r291.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal30.addChild(cube_r291);
		setRotationAngle(cube_r291, 0.0F, 0.0F, 0.2574F);
		cube_r291.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r292 = new FlowerPart(this);
		cube_r292.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal30.addChild(cube_r292);
		setRotationAngle(cube_r292, -0.0167F, 0.1453F, 0.7743F);
		cube_r292.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r293 = new FlowerPart(this);
		cube_r293.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal30.addChild(cube_r293);
		setRotationAngle(cube_r293, -0.0167F, -0.1339F, 0.7789F);
		cube_r293.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r294 = new FlowerPart(this);
		cube_r294.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal30.addChild(cube_r294);
		setRotationAngle(cube_r294, -0.0167F, -0.1339F, 0.7789F);
		cube_r294.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r295 = new FlowerPart(this);
		cube_r295.setRotationPoint(8.89F, 3.21F, -0.15F);
	}

	private void init50() {
		tepal30.addChild(cube_r295);
		setRotationAngle(cube_r295, -0.0165F, 0.0057F, 0.7766F);
		cube_r295.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r295.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r295.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r296 = new FlowerPart(this);
		cube_r296.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal30.addChild(cube_r296);
		setRotationAngle(cube_r296, -0.0139F, -0.0105F, -0.2006F);
		cube_r296.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r297 = new FlowerPart(this);
		cube_r297.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal30.addChild(cube_r297);
		setRotationAngle(cube_r297, 0.0015F, -0.0174F, -1.213F);
		cube_r297.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r298 = new FlowerPart(this);
		cube_r298.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal30.addChild(cube_r298);
		setRotationAngle(cube_r298, 0.0F, 0.1047F, -2.4696F);
		cube_r298.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r299 = new FlowerPart(this);
		cube_r299.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal30.addChild(cube_r299);
		setRotationAngle(cube_r299, 0.0F, -0.1047F, -2.4696F);
		cube_r299.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r300 = new FlowerPart(this);
		cube_r300.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal30.addChild(cube_r300);
		setRotationAngle(cube_r300, 0.0F, 0.0F, -2.4696F);
		cube_r300.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r300.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r300.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r301 = new FlowerPart(this);
		cube_r301.setRotationPoint(11.035F, 5.03F, 0.0F);
	}

	private void init51() {
		tepal30.addChild(cube_r301);
		setRotationAngle(cube_r301, 0.0F, 0.0F, 2.8536F);
		cube_r301.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r302 = new FlowerPart(this);
		cube_r302.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal30.addChild(cube_r302);
		setRotationAngle(cube_r302, 0.0F, -0.0873F, 1.8588F);
		cube_r302.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r303 = new FlowerPart(this);
		cube_r303.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal30.addChild(cube_r303);
		setRotationAngle(cube_r303, 0.0F, 0.1309F, 1.8588F);
		cube_r303.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r304 = new FlowerPart(this);
		cube_r304.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal30.addChild(cube_r304);
		setRotationAngle(cube_r304, 0.0F, 0.0F, 1.8588F);
		cube_r304.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r304.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r305 = new FlowerPart(this);
		cube_r305.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal30.addChild(cube_r305);
		setRotationAngle(cube_r305, 0.0F, 0.0F, 1.8588F);
		cube_r305.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r306 = new FlowerPart(this);
		cube_r306.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal30.addChild(cube_r306);
		setRotationAngle(cube_r306, 0.0F, 0.0F, 1.0036F);
		cube_r306.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r307 = new FlowerPart(this);
		cube_r307.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal30.addChild(cube_r307);
		setRotationAngle(cube_r307, 0.0F, -0.0873F, 0.5236F);
		cube_r307.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init52() {

		cube_r308 = new FlowerPart(this);
		cube_r308.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal30.addChild(cube_r308);
		setRotationAngle(cube_r308, 0.0F, 0.0873F, 0.5236F);
		cube_r308.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r309 = new FlowerPart(this);
		cube_r309.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal30.addChild(cube_r309);
		setRotationAngle(cube_r309, 0.0F, 0.0F, 0.5236F);
		cube_r309.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r309.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r310 = new FlowerPart(this);
		cube_r310.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal30.addChild(cube_r310);
		setRotationAngle(cube_r310, 0.0F, 0.0F, -0.1745F);
		cube_r310.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r311 = new FlowerPart(this);
		cube_r311.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal30.addChild(cube_r311);
		setRotationAngle(cube_r311, 0.0F, -0.2618F, -0.1745F);
		cube_r311.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r312 = new FlowerPart(this);
		cube_r312.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal30.addChild(cube_r312);
		setRotationAngle(cube_r312, 0.0F, 0.2618F, -0.1745F);
		cube_r312.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r313 = new FlowerPart(this);
		cube_r313.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal30.addChild(cube_r313);
		setRotationAngle(cube_r313, 0.0F, 0.0F, -0.1745F);
		cube_r313.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal25 = new FlowerPart(this);
		stemal25.setRotationPoint(0.0F, -1.0F, 0.0F);
	}

	private void init53() {
		perianth5.addChild(stemal25);
		setRotationAngle(stemal25, -0.4931F, -0.438F, -0.0571F);
		stemal25.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r314 = new FlowerPart(this);
		cube_r314.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal25.addChild(cube_r314);
		setRotationAngle(cube_r314, 0.0F, 0.0F, 0.8814F);
		cube_r314.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r315 = new FlowerPart(this);
		cube_r315.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal25.addChild(cube_r315);
		setRotationAngle(cube_r315, 0.0F, 0.0F, 0.2618F);
		cube_r315.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r316 = new FlowerPart(this);
		cube_r316.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal25.addChild(cube_r316);
		setRotationAngle(cube_r316, 0.0F, 0.0F, 0.8814F);
		cube_r316.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r317 = new FlowerPart(this);
		cube_r317.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal25.addChild(cube_r317);
		setRotationAngle(cube_r317, 0.0F, 0.0F, 0.5498F);
		cube_r317.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r318 = new FlowerPart(this);
		cube_r318.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal25.addChild(cube_r318);
		setRotationAngle(cube_r318, 0.0F, 0.0F, 0.8814F);
		cube_r318.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal26 = new FlowerPart(this);
		stemal26.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth5.addChild(stemal26);
		setRotationAngle(stemal26, -0.1017F, 0.0303F, 0.0497F);
		stemal26.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r319 = new FlowerPart(this);
	}

	private void init54() {
		cube_r319.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal26.addChild(cube_r319);
		setRotationAngle(cube_r319, 0.0F, 0.0F, 0.8814F);
		cube_r319.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r320 = new FlowerPart(this);
		cube_r320.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal26.addChild(cube_r320);
		setRotationAngle(cube_r320, 0.0F, 0.0F, 0.2618F);
		cube_r320.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r321 = new FlowerPart(this);
		cube_r321.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal26.addChild(cube_r321);
		setRotationAngle(cube_r321, 0.0F, 0.0F, 0.8814F);
		cube_r321.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r322 = new FlowerPart(this);
		cube_r322.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal26.addChild(cube_r322);
		setRotationAngle(cube_r322, 0.0F, 0.0F, 0.5498F);
		cube_r322.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r323 = new FlowerPart(this);
		cube_r323.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal26.addChild(cube_r323);
		setRotationAngle(cube_r323, 0.0F, 0.0F, 0.8814F);
		cube_r323.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal27 = new FlowerPart(this);
		stemal27.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth5.addChild(stemal27);
		setRotationAngle(stemal27, 0.1582F, 0.2804F, -0.7053F);
		stemal27.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r324 = new FlowerPart(this);
		cube_r324.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal27.addChild(cube_r324);
		setRotationAngle(cube_r324, 0.0F, 0.0F, 0.8814F);
		cube_r324.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));
	}

	private void init55() {

		cube_r325 = new FlowerPart(this);
		cube_r325.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal27.addChild(cube_r325);
		setRotationAngle(cube_r325, 0.0F, 0.0F, 0.2618F);
		cube_r325.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r326 = new FlowerPart(this);
		cube_r326.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal27.addChild(cube_r326);
		setRotationAngle(cube_r326, 0.0F, 0.0F, 0.8814F);
		cube_r326.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r327 = new FlowerPart(this);
		cube_r327.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal27.addChild(cube_r327);
		setRotationAngle(cube_r327, 0.0F, 0.0F, 0.5498F);
		cube_r327.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r328 = new FlowerPart(this);
		cube_r328.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal27.addChild(cube_r328);
		setRotationAngle(cube_r328, 0.0F, 0.0F, 0.8814F);
		cube_r328.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal28 = new FlowerPart(this);
		stemal28.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth5.addChild(stemal28);
		setRotationAngle(stemal28, -0.2949F, -0.1812F, -0.7237F);
		stemal28.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r329 = new FlowerPart(this);
		cube_r329.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal28.addChild(cube_r329);
		setRotationAngle(cube_r329, 0.0F, 0.0F, 0.8814F);
		cube_r329.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r330 = new FlowerPart(this);
		cube_r330.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal28.addChild(cube_r330);
	}

	private void init56() {
		setRotationAngle(cube_r330, 0.0F, 0.0F, 0.2618F);
		cube_r330.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r331 = new FlowerPart(this);
		cube_r331.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal28.addChild(cube_r331);
		setRotationAngle(cube_r331, 0.0F, 0.0F, 0.8814F);
		cube_r331.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r332 = new FlowerPart(this);
		cube_r332.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal28.addChild(cube_r332);
		setRotationAngle(cube_r332, 0.0F, 0.0F, 0.5498F);
		cube_r332.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r333 = new FlowerPart(this);
		cube_r333.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal28.addChild(cube_r333);
		setRotationAngle(cube_r333, 0.0F, 0.0F, 0.8814F);
		cube_r333.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal29 = new FlowerPart(this);
		stemal29.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth5.addChild(stemal29);
		setRotationAngle(stemal29, 0.3149F, 0.1775F, -0.2915F);
		stemal29.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r334 = new FlowerPart(this);
		cube_r334.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal29.addChild(cube_r334);
		setRotationAngle(cube_r334, 0.0F, 0.0F, 0.8814F);
		cube_r334.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r335 = new FlowerPart(this);
		cube_r335.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal29.addChild(cube_r335);
		setRotationAngle(cube_r335, 0.0F, 0.0F, 0.2618F);
		cube_r335.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r336 = new FlowerPart(this);
	}

	private void init57() {
		cube_r336.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal29.addChild(cube_r336);
		setRotationAngle(cube_r336, 0.0F, 0.0F, 0.8814F);
		cube_r336.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r337 = new FlowerPart(this);
		cube_r337.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal29.addChild(cube_r337);
		setRotationAngle(cube_r337, 0.0F, 0.0F, 0.5498F);
		cube_r337.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r338 = new FlowerPart(this);
		cube_r338.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal29.addChild(cube_r338);
		setRotationAngle(cube_r338, 0.0F, 0.0F, 0.8814F);
		cube_r338.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal30 = new FlowerPart(this);
		stemal30.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth5.addChild(stemal30);
		setRotationAngle(stemal30, 0.0F, 0.0F, -0.9163F);
		stemal30.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r339 = new FlowerPart(this);
		cube_r339.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal30.addChild(cube_r339);
		setRotationAngle(cube_r339, 0.0F, 0.0F, 0.8814F);
		cube_r339.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r340 = new FlowerPart(this);
		cube_r340.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal30.addChild(cube_r340);
		setRotationAngle(cube_r340, 0.0F, 0.0F, 0.2618F);
		cube_r340.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r341 = new FlowerPart(this);
		cube_r341.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal30.addChild(cube_r341);
		setRotationAngle(cube_r341, 0.0F, 0.0F, 0.8814F);
		cube_r341.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));
	}

	private void init58() {

		cube_r342 = new FlowerPart(this);
		cube_r342.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal30.addChild(cube_r342);
		setRotationAngle(cube_r342, 0.0F, 0.0F, 0.5498F);
		cube_r342.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r343 = new FlowerPart(this);
		cube_r343.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal30.addChild(cube_r343);
		setRotationAngle(cube_r343, 0.0F, 0.0F, 0.8814F);
		cube_r343.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		perianth6 = new FlowerPart(this);
		perianth6.setRotationPoint(-20.04F, -2.365F, -0.73F);
		half1.addChild(perianth6);
		setRotationAngle(perianth6, -0.4291F, 0.5638F, -0.7679F);
		

		tepal31 = new FlowerPart(this);
		tepal31.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth6.addChild(tepal31);
		setRotationAngle(tepal31, 0.0F, 0.0F, -0.6109F);
		

		cube_r344 = new FlowerPart(this);
		cube_r344.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal31.addChild(cube_r344);
		setRotationAngle(cube_r344, 0.0F, 0.0F, 0.2574F);
		cube_r344.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r345 = new FlowerPart(this);
		cube_r345.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal31.addChild(cube_r345);
		setRotationAngle(cube_r345, -0.0167F, 0.1453F, 0.7743F);
		cube_r345.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r346 = new FlowerPart(this);
		cube_r346.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal31.addChild(cube_r346);
	}

	private void init59() {
		setRotationAngle(cube_r346, -0.0167F, -0.1339F, 0.7789F);
		cube_r346.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r347 = new FlowerPart(this);
		cube_r347.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal31.addChild(cube_r347);
		setRotationAngle(cube_r347, -0.0167F, -0.1339F, 0.7789F);
		cube_r347.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r348 = new FlowerPart(this);
		cube_r348.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal31.addChild(cube_r348);
		setRotationAngle(cube_r348, -0.0165F, 0.0057F, 0.7766F);
		cube_r348.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r348.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r348.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r349 = new FlowerPart(this);
		cube_r349.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal31.addChild(cube_r349);
		setRotationAngle(cube_r349, -0.0139F, -0.0105F, -0.2006F);
		cube_r349.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r350 = new FlowerPart(this);
		cube_r350.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal31.addChild(cube_r350);
		setRotationAngle(cube_r350, 0.0015F, -0.0174F, -1.213F);
		cube_r350.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r351 = new FlowerPart(this);
		cube_r351.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal31.addChild(cube_r351);
		setRotationAngle(cube_r351, 0.0F, 0.1047F, -2.4696F);
		cube_r351.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r352 = new FlowerPart(this);
		cube_r352.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal31.addChild(cube_r352);
		setRotationAngle(cube_r352, 0.0F, -0.1047F, -2.4696F);
		cube_r352.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init60() {

		cube_r353 = new FlowerPart(this);
		cube_r353.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal31.addChild(cube_r353);
		setRotationAngle(cube_r353, 0.0F, 0.0F, -2.4696F);
		cube_r353.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r353.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r353.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r354 = new FlowerPart(this);
		cube_r354.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal31.addChild(cube_r354);
		setRotationAngle(cube_r354, 0.0F, 0.0F, 2.8536F);
		cube_r354.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r355 = new FlowerPart(this);
		cube_r355.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal31.addChild(cube_r355);
		setRotationAngle(cube_r355, 0.0F, -0.0873F, 1.8588F);
		cube_r355.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r356 = new FlowerPart(this);
		cube_r356.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal31.addChild(cube_r356);
		setRotationAngle(cube_r356, 0.0F, 0.1309F, 1.8588F);
		cube_r356.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r357 = new FlowerPart(this);
		cube_r357.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal31.addChild(cube_r357);
		setRotationAngle(cube_r357, 0.0F, 0.0F, 1.8588F);
		cube_r357.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r357.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r358 = new FlowerPart(this);
		cube_r358.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal31.addChild(cube_r358);
		setRotationAngle(cube_r358, 0.0F, 0.0F, 1.8588F);
		cube_r358.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r359 = new FlowerPart(this);
	}

	private void init61() {
		cube_r359.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal31.addChild(cube_r359);
		setRotationAngle(cube_r359, 0.0F, 0.0F, 1.0036F);
		cube_r359.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r360 = new FlowerPart(this);
		cube_r360.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal31.addChild(cube_r360);
		setRotationAngle(cube_r360, 0.0F, -0.0873F, 0.5236F);
		cube_r360.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r361 = new FlowerPart(this);
		cube_r361.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal31.addChild(cube_r361);
		setRotationAngle(cube_r361, 0.0F, 0.0873F, 0.5236F);
		cube_r361.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r362 = new FlowerPart(this);
		cube_r362.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal31.addChild(cube_r362);
		setRotationAngle(cube_r362, 0.0F, 0.0F, 0.5236F);
		cube_r362.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r362.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r363 = new FlowerPart(this);
		cube_r363.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal31.addChild(cube_r363);
		setRotationAngle(cube_r363, 0.0F, 0.0F, -0.1745F);
		cube_r363.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r364 = new FlowerPart(this);
		cube_r364.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal31.addChild(cube_r364);
		setRotationAngle(cube_r364, 0.0F, -0.2618F, -0.1745F);
		cube_r364.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r365 = new FlowerPart(this);
		cube_r365.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal31.addChild(cube_r365);
		setRotationAngle(cube_r365, 0.0F, 0.2618F, -0.1745F);
	}

	private void init62() {
		cube_r365.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r366 = new FlowerPart(this);
		cube_r366.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal31.addChild(cube_r366);
		setRotationAngle(cube_r366, 0.0F, 0.0F, -0.1745F);
		cube_r366.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal32 = new FlowerPart(this);
		tepal32.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth6.addChild(tepal32);
		setRotationAngle(tepal32, -0.5056F, 0.9128F, -0.8547F);
		

		cube_r367 = new FlowerPart(this);
		cube_r367.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal32.addChild(cube_r367);
		setRotationAngle(cube_r367, 0.0F, 0.0F, 0.2574F);
		cube_r367.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r368 = new FlowerPart(this);
		cube_r368.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal32.addChild(cube_r368);
		setRotationAngle(cube_r368, -0.0167F, 0.1453F, 0.7743F);
		cube_r368.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r369 = new FlowerPart(this);
		cube_r369.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal32.addChild(cube_r369);
		setRotationAngle(cube_r369, -0.0167F, -0.1339F, 0.7789F);
		cube_r369.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r370 = new FlowerPart(this);
		cube_r370.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal32.addChild(cube_r370);
		setRotationAngle(cube_r370, -0.0167F, -0.1339F, 0.7789F);
		cube_r370.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r371 = new FlowerPart(this);
		cube_r371.setRotationPoint(8.89F, 3.21F, -0.15F);
	}

	private void init63() {
		tepal32.addChild(cube_r371);
		setRotationAngle(cube_r371, -0.0165F, 0.0057F, 0.7766F);
		cube_r371.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r371.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r371.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r372 = new FlowerPart(this);
		cube_r372.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal32.addChild(cube_r372);
		setRotationAngle(cube_r372, -0.0139F, -0.0105F, -0.2006F);
		cube_r372.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r373 = new FlowerPart(this);
		cube_r373.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal32.addChild(cube_r373);
		setRotationAngle(cube_r373, 0.0015F, -0.0174F, -1.213F);
		cube_r373.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r374 = new FlowerPart(this);
		cube_r374.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal32.addChild(cube_r374);
		setRotationAngle(cube_r374, 0.0F, 0.1047F, -2.4696F);
		cube_r374.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r375 = new FlowerPart(this);
		cube_r375.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal32.addChild(cube_r375);
		setRotationAngle(cube_r375, 0.0F, -0.1047F, -2.4696F);
		cube_r375.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r376 = new FlowerPart(this);
		cube_r376.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal32.addChild(cube_r376);
		setRotationAngle(cube_r376, 0.0F, 0.0F, -2.4696F);
		cube_r376.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r376.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r376.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r377 = new FlowerPart(this);
		cube_r377.setRotationPoint(11.035F, 5.03F, 0.0F);
	}

	private void init64() {
		tepal32.addChild(cube_r377);
		setRotationAngle(cube_r377, 0.0F, 0.0F, 2.8536F);
		cube_r377.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r378 = new FlowerPart(this);
		cube_r378.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal32.addChild(cube_r378);
		setRotationAngle(cube_r378, 0.0F, -0.0873F, 1.8588F);
		cube_r378.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r379 = new FlowerPart(this);
		cube_r379.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal32.addChild(cube_r379);
		setRotationAngle(cube_r379, 0.0F, 0.1309F, 1.8588F);
		cube_r379.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r380 = new FlowerPart(this);
		cube_r380.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal32.addChild(cube_r380);
		setRotationAngle(cube_r380, 0.0F, 0.0F, 1.8588F);
		cube_r380.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r380.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r381 = new FlowerPart(this);
		cube_r381.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal32.addChild(cube_r381);
		setRotationAngle(cube_r381, 0.0F, 0.0F, 1.8588F);
		cube_r381.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r382 = new FlowerPart(this);
		cube_r382.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal32.addChild(cube_r382);
		setRotationAngle(cube_r382, 0.0F, 0.0F, 1.0036F);
		cube_r382.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r383 = new FlowerPart(this);
		cube_r383.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal32.addChild(cube_r383);
		setRotationAngle(cube_r383, 0.0F, -0.0873F, 0.5236F);
		cube_r383.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init65() {

		cube_r384 = new FlowerPart(this);
		cube_r384.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal32.addChild(cube_r384);
		setRotationAngle(cube_r384, 0.0F, 0.0873F, 0.5236F);
		cube_r384.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r385 = new FlowerPart(this);
		cube_r385.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal32.addChild(cube_r385);
		setRotationAngle(cube_r385, 0.0F, 0.0F, 0.5236F);
		cube_r385.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r385.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r386 = new FlowerPart(this);
		cube_r386.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal32.addChild(cube_r386);
		setRotationAngle(cube_r386, 0.0F, 0.0F, -0.1745F);
		cube_r386.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r387 = new FlowerPart(this);
		cube_r387.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal32.addChild(cube_r387);
		setRotationAngle(cube_r387, 0.0F, -0.2618F, -0.1745F);
		cube_r387.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r388 = new FlowerPart(this);
		cube_r388.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal32.addChild(cube_r388);
		setRotationAngle(cube_r388, 0.0F, 0.2618F, -0.1745F);
		cube_r388.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r389 = new FlowerPart(this);
		cube_r389.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal32.addChild(cube_r389);
		setRotationAngle(cube_r389, 0.0F, 0.0F, -0.1745F);
		cube_r389.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal33 = new FlowerPart(this);
		tepal33.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	private void init66() {
		perianth6.addChild(tepal33);
		setRotationAngle(tepal33, -2.6068F, 0.9507F, -2.7742F);
		

		cube_r390 = new FlowerPart(this);
		cube_r390.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal33.addChild(cube_r390);
		setRotationAngle(cube_r390, 0.0F, 0.0F, 0.2574F);
		cube_r390.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r391 = new FlowerPart(this);
		cube_r391.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal33.addChild(cube_r391);
		setRotationAngle(cube_r391, -0.0167F, 0.1453F, 0.7743F);
		cube_r391.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r392 = new FlowerPart(this);
		cube_r392.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal33.addChild(cube_r392);
		setRotationAngle(cube_r392, -0.0167F, -0.1339F, 0.7789F);
		cube_r392.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r393 = new FlowerPart(this);
		cube_r393.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal33.addChild(cube_r393);
		setRotationAngle(cube_r393, -0.0167F, -0.1339F, 0.7789F);
		cube_r393.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r394 = new FlowerPart(this);
		cube_r394.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal33.addChild(cube_r394);
		setRotationAngle(cube_r394, -0.0165F, 0.0057F, 0.7766F);
		cube_r394.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r394.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r394.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r395 = new FlowerPart(this);
		cube_r395.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal33.addChild(cube_r395);
		setRotationAngle(cube_r395, -0.0139F, -0.0105F, -0.2006F);
	}

	private void init67() {
		cube_r395.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r396 = new FlowerPart(this);
		cube_r396.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal33.addChild(cube_r396);
		setRotationAngle(cube_r396, 0.0015F, -0.0174F, -1.213F);
		cube_r396.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r397 = new FlowerPart(this);
		cube_r397.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal33.addChild(cube_r397);
		setRotationAngle(cube_r397, 0.0F, 0.1047F, -2.4696F);
		cube_r397.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r398 = new FlowerPart(this);
		cube_r398.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal33.addChild(cube_r398);
		setRotationAngle(cube_r398, 0.0F, -0.1047F, -2.4696F);
		cube_r398.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r399 = new FlowerPart(this);
		cube_r399.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal33.addChild(cube_r399);
		setRotationAngle(cube_r399, 0.0F, 0.0F, -2.4696F);
		cube_r399.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r399.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r399.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r400 = new FlowerPart(this);
		cube_r400.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal33.addChild(cube_r400);
		setRotationAngle(cube_r400, 0.0F, 0.0F, 2.8536F);
		cube_r400.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r401 = new FlowerPart(this);
		cube_r401.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal33.addChild(cube_r401);
		setRotationAngle(cube_r401, 0.0F, -0.0873F, 1.8588F);
		cube_r401.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r402 = new FlowerPart(this);
	}

	private void init68() {
		cube_r402.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal33.addChild(cube_r402);
		setRotationAngle(cube_r402, 0.0F, 0.1309F, 1.8588F);
		cube_r402.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r403 = new FlowerPart(this);
		cube_r403.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal33.addChild(cube_r403);
		setRotationAngle(cube_r403, 0.0F, 0.0F, 1.8588F);
		cube_r403.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r403.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r404 = new FlowerPart(this);
		cube_r404.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal33.addChild(cube_r404);
		setRotationAngle(cube_r404, 0.0F, 0.0F, 1.8588F);
		cube_r404.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r405 = new FlowerPart(this);
		cube_r405.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal33.addChild(cube_r405);
		setRotationAngle(cube_r405, 0.0F, 0.0F, 1.0036F);
		cube_r405.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r406 = new FlowerPart(this);
		cube_r406.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal33.addChild(cube_r406);
		setRotationAngle(cube_r406, 0.0F, -0.0873F, 0.5236F);
		cube_r406.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r407 = new FlowerPart(this);
		cube_r407.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal33.addChild(cube_r407);
		setRotationAngle(cube_r407, 0.0F, 0.0873F, 0.5236F);
		cube_r407.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r408 = new FlowerPart(this);
		cube_r408.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal33.addChild(cube_r408);
		setRotationAngle(cube_r408, 0.0F, 0.0F, 0.5236F);
	}

	private void init69() {
		cube_r408.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r408.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r409 = new FlowerPart(this);
		cube_r409.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal33.addChild(cube_r409);
		setRotationAngle(cube_r409, 0.0F, 0.0F, -0.1745F);
		cube_r409.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r410 = new FlowerPart(this);
		cube_r410.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal33.addChild(cube_r410);
		setRotationAngle(cube_r410, 0.0F, -0.2618F, -0.1745F);
		cube_r410.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r411 = new FlowerPart(this);
		cube_r411.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal33.addChild(cube_r411);
		setRotationAngle(cube_r411, 0.0F, 0.2618F, -0.1745F);
		cube_r411.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r412 = new FlowerPart(this);
		cube_r412.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal33.addChild(cube_r412);
		setRotationAngle(cube_r412, 0.0F, 0.0F, -0.1745F);
		cube_r412.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal34 = new FlowerPart(this);
		tepal34.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth6.addChild(tepal34);
		setRotationAngle(tepal34, -3.1416F, 0.0F, -3.0543F);
		

		cube_r413 = new FlowerPart(this);
		cube_r413.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal34.addChild(cube_r413);
		setRotationAngle(cube_r413, 0.0F, 0.0F, 0.2574F);
		cube_r413.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r414 = new FlowerPart(this);
	}

	private void init70() {
		cube_r414.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal34.addChild(cube_r414);
		setRotationAngle(cube_r414, -0.0167F, 0.1453F, 0.7743F);
		cube_r414.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r415 = new FlowerPart(this);
		cube_r415.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal34.addChild(cube_r415);
		setRotationAngle(cube_r415, -0.0167F, -0.1339F, 0.7789F);
		cube_r415.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r416 = new FlowerPart(this);
		cube_r416.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal34.addChild(cube_r416);
		setRotationAngle(cube_r416, -0.0167F, -0.1339F, 0.7789F);
		cube_r416.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r417 = new FlowerPart(this);
		cube_r417.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal34.addChild(cube_r417);
		setRotationAngle(cube_r417, -0.0165F, 0.0057F, 0.7766F);
		cube_r417.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r417.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r417.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r418 = new FlowerPart(this);
		cube_r418.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal34.addChild(cube_r418);
		setRotationAngle(cube_r418, -0.0139F, -0.0105F, -0.2006F);
		cube_r418.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r419 = new FlowerPart(this);
		cube_r419.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal34.addChild(cube_r419);
		setRotationAngle(cube_r419, 0.0015F, -0.0174F, -1.213F);
		cube_r419.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r420 = new FlowerPart(this);
		cube_r420.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal34.addChild(cube_r420);
	}

	private void init71() {
		setRotationAngle(cube_r420, 0.0F, 0.1047F, -2.4696F);
		cube_r420.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r421 = new FlowerPart(this);
		cube_r421.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal34.addChild(cube_r421);
		setRotationAngle(cube_r421, 0.0F, -0.1047F, -2.4696F);
		cube_r421.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r422 = new FlowerPart(this);
		cube_r422.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal34.addChild(cube_r422);
		setRotationAngle(cube_r422, 0.0F, 0.0F, -2.4696F);
		cube_r422.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r422.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r422.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r423 = new FlowerPart(this);
		cube_r423.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal34.addChild(cube_r423);
		setRotationAngle(cube_r423, 0.0F, 0.0F, 2.8536F);
		cube_r423.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r424 = new FlowerPart(this);
		cube_r424.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal34.addChild(cube_r424);
		setRotationAngle(cube_r424, 0.0F, -0.0873F, 1.8588F);
		cube_r424.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r425 = new FlowerPart(this);
		cube_r425.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal34.addChild(cube_r425);
		setRotationAngle(cube_r425, 0.0F, 0.1309F, 1.8588F);
		cube_r425.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r426 = new FlowerPart(this);
		cube_r426.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal34.addChild(cube_r426);
		setRotationAngle(cube_r426, 0.0F, 0.0F, 1.8588F);
		cube_r426.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
	}

	private void init72() {
		cube_r426.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r427 = new FlowerPart(this);
		cube_r427.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal34.addChild(cube_r427);
		setRotationAngle(cube_r427, 0.0F, 0.0F, 1.8588F);
		cube_r427.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r428 = new FlowerPart(this);
		cube_r428.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal34.addChild(cube_r428);
		setRotationAngle(cube_r428, 0.0F, 0.0F, 1.0036F);
		cube_r428.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r429 = new FlowerPart(this);
		cube_r429.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal34.addChild(cube_r429);
		setRotationAngle(cube_r429, 0.0F, -0.0873F, 0.5236F);
		cube_r429.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r430 = new FlowerPart(this);
		cube_r430.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal34.addChild(cube_r430);
		setRotationAngle(cube_r430, 0.0F, 0.0873F, 0.5236F);
		cube_r430.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r431 = new FlowerPart(this);
		cube_r431.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal34.addChild(cube_r431);
		setRotationAngle(cube_r431, 0.0F, 0.0F, 0.5236F);
		cube_r431.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r431.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r432 = new FlowerPart(this);
		cube_r432.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal34.addChild(cube_r432);
		setRotationAngle(cube_r432, 0.0F, 0.0F, -0.1745F);
		cube_r432.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r433 = new FlowerPart(this);
	}

	private void init73() {
		cube_r433.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal34.addChild(cube_r433);
		setRotationAngle(cube_r433, 0.0F, -0.2618F, -0.1745F);
		cube_r433.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r434 = new FlowerPart(this);
		cube_r434.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal34.addChild(cube_r434);
		setRotationAngle(cube_r434, 0.0F, 0.2618F, -0.1745F);
		cube_r434.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r435 = new FlowerPart(this);
		cube_r435.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal34.addChild(cube_r435);
		setRotationAngle(cube_r435, 0.0F, 0.0F, -0.1745F);
		cube_r435.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal35 = new FlowerPart(this);
		tepal35.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth6.addChild(tepal35);
		setRotationAngle(tepal35, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r436 = new FlowerPart(this);
		cube_r436.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal35.addChild(cube_r436);
		setRotationAngle(cube_r436, 0.0F, 0.0F, 0.2574F);
		cube_r436.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r437 = new FlowerPart(this);
		cube_r437.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal35.addChild(cube_r437);
		setRotationAngle(cube_r437, -0.0167F, 0.1453F, 0.7743F);
		cube_r437.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r438 = new FlowerPart(this);
		cube_r438.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal35.addChild(cube_r438);
		setRotationAngle(cube_r438, -0.0167F, -0.1339F, 0.7789F);
		cube_r438.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init74() {

		cube_r439 = new FlowerPart(this);
		cube_r439.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal35.addChild(cube_r439);
		setRotationAngle(cube_r439, -0.0167F, -0.1339F, 0.7789F);
		cube_r439.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r440 = new FlowerPart(this);
		cube_r440.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal35.addChild(cube_r440);
		setRotationAngle(cube_r440, -0.0165F, 0.0057F, 0.7766F);
		cube_r440.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r440.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r440.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r441 = new FlowerPart(this);
		cube_r441.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal35.addChild(cube_r441);
		setRotationAngle(cube_r441, -0.0139F, -0.0105F, -0.2006F);
		cube_r441.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r442 = new FlowerPart(this);
		cube_r442.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal35.addChild(cube_r442);
		setRotationAngle(cube_r442, 0.0015F, -0.0174F, -1.213F);
		cube_r442.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r443 = new FlowerPart(this);
		cube_r443.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal35.addChild(cube_r443);
		setRotationAngle(cube_r443, 0.0F, 0.1047F, -2.4696F);
		cube_r443.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r444 = new FlowerPart(this);
		cube_r444.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal35.addChild(cube_r444);
		setRotationAngle(cube_r444, 0.0F, -0.1047F, -2.4696F);
		cube_r444.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r445 = new FlowerPart(this);
	}

	private void init75() {
		cube_r445.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal35.addChild(cube_r445);
		setRotationAngle(cube_r445, 0.0F, 0.0F, -2.4696F);
		cube_r445.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r445.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r445.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r446 = new FlowerPart(this);
		cube_r446.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal35.addChild(cube_r446);
		setRotationAngle(cube_r446, 0.0F, 0.0F, 2.8536F);
		cube_r446.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r447 = new FlowerPart(this);
		cube_r447.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal35.addChild(cube_r447);
		setRotationAngle(cube_r447, 0.0F, -0.0873F, 1.8588F);
		cube_r447.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r448 = new FlowerPart(this);
		cube_r448.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal35.addChild(cube_r448);
		setRotationAngle(cube_r448, 0.0F, 0.1309F, 1.8588F);
		cube_r448.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r449 = new FlowerPart(this);
		cube_r449.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal35.addChild(cube_r449);
		setRotationAngle(cube_r449, 0.0F, 0.0F, 1.8588F);
		cube_r449.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r449.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r450 = new FlowerPart(this);
		cube_r450.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal35.addChild(cube_r450);
		setRotationAngle(cube_r450, 0.0F, 0.0F, 1.8588F);
		cube_r450.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r451 = new FlowerPart(this);
		cube_r451.setRotationPoint(8.81F, 0.575F, 0.0F);
	}

	private void init76() {
		tepal35.addChild(cube_r451);
		setRotationAngle(cube_r451, 0.0F, 0.0F, 1.0036F);
		cube_r451.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r452 = new FlowerPart(this);
		cube_r452.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal35.addChild(cube_r452);
		setRotationAngle(cube_r452, 0.0F, -0.0873F, 0.5236F);
		cube_r452.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r453 = new FlowerPart(this);
		cube_r453.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal35.addChild(cube_r453);
		setRotationAngle(cube_r453, 0.0F, 0.0873F, 0.5236F);
		cube_r453.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r454 = new FlowerPart(this);
		cube_r454.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal35.addChild(cube_r454);
		setRotationAngle(cube_r454, 0.0F, 0.0F, 0.5236F);
		cube_r454.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r454.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r455 = new FlowerPart(this);
		cube_r455.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal35.addChild(cube_r455);
		setRotationAngle(cube_r455, 0.0F, 0.0F, -0.1745F);
		cube_r455.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r456 = new FlowerPart(this);
		cube_r456.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal35.addChild(cube_r456);
		setRotationAngle(cube_r456, 0.0F, -0.2618F, -0.1745F);
		cube_r456.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r457 = new FlowerPart(this);
		cube_r457.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal35.addChild(cube_r457);
		setRotationAngle(cube_r457, 0.0F, 0.2618F, -0.1745F);
		cube_r457.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init77() {

		cube_r458 = new FlowerPart(this);
		cube_r458.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal35.addChild(cube_r458);
		setRotationAngle(cube_r458, 0.0F, 0.0F, -0.1745F);
		cube_r458.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal36 = new FlowerPart(this);
		tepal36.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth6.addChild(tepal36);
		setRotationAngle(tepal36, 0.5348F, -0.9507F, -0.891F);
		

		cube_r459 = new FlowerPart(this);
		cube_r459.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal36.addChild(cube_r459);
		setRotationAngle(cube_r459, 0.0F, 0.0F, 0.2574F);
		cube_r459.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r460 = new FlowerPart(this);
		cube_r460.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal36.addChild(cube_r460);
		setRotationAngle(cube_r460, -0.0167F, 0.1453F, 0.7743F);
		cube_r460.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r461 = new FlowerPart(this);
		cube_r461.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal36.addChild(cube_r461);
		setRotationAngle(cube_r461, -0.0167F, -0.1339F, 0.7789F);
		cube_r461.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r462 = new FlowerPart(this);
		cube_r462.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal36.addChild(cube_r462);
		setRotationAngle(cube_r462, -0.0167F, -0.1339F, 0.7789F);
		cube_r462.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r463 = new FlowerPart(this);
		cube_r463.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal36.addChild(cube_r463);
	}

	private void init78() {
		setRotationAngle(cube_r463, -0.0165F, 0.0057F, 0.7766F);
		cube_r463.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r463.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r463.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r464 = new FlowerPart(this);
		cube_r464.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal36.addChild(cube_r464);
		setRotationAngle(cube_r464, -0.0139F, -0.0105F, -0.2006F);
		cube_r464.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r465 = new FlowerPart(this);
		cube_r465.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal36.addChild(cube_r465);
		setRotationAngle(cube_r465, 0.0015F, -0.0174F, -1.213F);
		cube_r465.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r466 = new FlowerPart(this);
		cube_r466.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal36.addChild(cube_r466);
		setRotationAngle(cube_r466, 0.0F, 0.1047F, -2.4696F);
		cube_r466.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r467 = new FlowerPart(this);
		cube_r467.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal36.addChild(cube_r467);
		setRotationAngle(cube_r467, 0.0F, -0.1047F, -2.4696F);
		cube_r467.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r468 = new FlowerPart(this);
		cube_r468.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal36.addChild(cube_r468);
		setRotationAngle(cube_r468, 0.0F, 0.0F, -2.4696F);
		cube_r468.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r468.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r468.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r469 = new FlowerPart(this);
		cube_r469.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal36.addChild(cube_r469);
	}

	private void init79() {
		setRotationAngle(cube_r469, 0.0F, 0.0F, 2.8536F);
		cube_r469.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r470 = new FlowerPart(this);
		cube_r470.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal36.addChild(cube_r470);
		setRotationAngle(cube_r470, 0.0F, -0.0873F, 1.8588F);
		cube_r470.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r471 = new FlowerPart(this);
		cube_r471.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal36.addChild(cube_r471);
		setRotationAngle(cube_r471, 0.0F, 0.1309F, 1.8588F);
		cube_r471.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r472 = new FlowerPart(this);
		cube_r472.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal36.addChild(cube_r472);
		setRotationAngle(cube_r472, 0.0F, 0.0F, 1.8588F);
		cube_r472.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r472.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r473 = new FlowerPart(this);
		cube_r473.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal36.addChild(cube_r473);
		setRotationAngle(cube_r473, 0.0F, 0.0F, 1.8588F);
		cube_r473.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r474 = new FlowerPart(this);
		cube_r474.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal36.addChild(cube_r474);
		setRotationAngle(cube_r474, 0.0F, 0.0F, 1.0036F);
		cube_r474.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r475 = new FlowerPart(this);
		cube_r475.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal36.addChild(cube_r475);
		setRotationAngle(cube_r475, 0.0F, -0.0873F, 0.5236F);
		cube_r475.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r476 = new FlowerPart(this);
	}

	private void init80() {
		cube_r476.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal36.addChild(cube_r476);
		setRotationAngle(cube_r476, 0.0F, 0.0873F, 0.5236F);
		cube_r476.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r477 = new FlowerPart(this);
		cube_r477.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal36.addChild(cube_r477);
		setRotationAngle(cube_r477, 0.0F, 0.0F, 0.5236F);
		cube_r477.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r477.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r478 = new FlowerPart(this);
		cube_r478.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal36.addChild(cube_r478);
		setRotationAngle(cube_r478, 0.0F, 0.0F, -0.1745F);
		cube_r478.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r479 = new FlowerPart(this);
		cube_r479.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal36.addChild(cube_r479);
		setRotationAngle(cube_r479, 0.0F, -0.2618F, -0.1745F);
		cube_r479.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r480 = new FlowerPart(this);
		cube_r480.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal36.addChild(cube_r480);
		setRotationAngle(cube_r480, 0.0F, 0.2618F, -0.1745F);
		cube_r480.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r481 = new FlowerPart(this);
		cube_r481.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal36.addChild(cube_r481);
		setRotationAngle(cube_r481, 0.0F, 0.0F, -0.1745F);
		cube_r481.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal31 = new FlowerPart(this);
		stemal31.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal31);
		setRotationAngle(stemal31, -0.4931F, -0.438F, -0.0571F);
	}

	private void init81() {
		stemal31.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r482 = new FlowerPart(this);
		cube_r482.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal31.addChild(cube_r482);
		setRotationAngle(cube_r482, 0.0F, 0.0F, 0.8814F);
		cube_r482.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r483 = new FlowerPart(this);
		cube_r483.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal31.addChild(cube_r483);
		setRotationAngle(cube_r483, 0.0F, 0.0F, 0.2618F);
		cube_r483.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r484 = new FlowerPart(this);
		cube_r484.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal31.addChild(cube_r484);
		setRotationAngle(cube_r484, 0.0F, 0.0F, 0.8814F);
		cube_r484.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r485 = new FlowerPart(this);
		cube_r485.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal31.addChild(cube_r485);
		setRotationAngle(cube_r485, 0.0F, 0.0F, 0.5498F);
		cube_r485.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r486 = new FlowerPart(this);
		cube_r486.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal31.addChild(cube_r486);
		setRotationAngle(cube_r486, 0.0F, 0.0F, 0.8814F);
		cube_r486.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal32 = new FlowerPart(this);
		stemal32.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal32);
		setRotationAngle(stemal32, -0.1017F, 0.0303F, 0.0497F);
		stemal32.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r487 = new FlowerPart(this);
		cube_r487.setRotationPoint(7.105F, -13.29F, 0.145F);
	}

	private void init82() {
		stemal32.addChild(cube_r487);
		setRotationAngle(cube_r487, 0.0F, 0.0F, 0.8814F);
		cube_r487.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r488 = new FlowerPart(this);
		cube_r488.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal32.addChild(cube_r488);
		setRotationAngle(cube_r488, 0.0F, 0.0F, 0.2618F);
		cube_r488.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r489 = new FlowerPart(this);
		cube_r489.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal32.addChild(cube_r489);
		setRotationAngle(cube_r489, 0.0F, 0.0F, 0.8814F);
		cube_r489.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r490 = new FlowerPart(this);
		cube_r490.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal32.addChild(cube_r490);
		setRotationAngle(cube_r490, 0.0F, 0.0F, 0.5498F);
		cube_r490.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r491 = new FlowerPart(this);
		cube_r491.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal32.addChild(cube_r491);
		setRotationAngle(cube_r491, 0.0F, 0.0F, 0.8814F);
		cube_r491.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal33 = new FlowerPart(this);
		stemal33.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal33);
		setRotationAngle(stemal33, 0.1582F, 0.2804F, -0.7053F);
		stemal33.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r492 = new FlowerPart(this);
		cube_r492.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal33.addChild(cube_r492);
		setRotationAngle(cube_r492, 0.0F, 0.0F, 0.8814F);
		cube_r492.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r493 = new FlowerPart(this);
	}

	private void init83() {
		cube_r493.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal33.addChild(cube_r493);
		setRotationAngle(cube_r493, 0.0F, 0.0F, 0.2618F);
		cube_r493.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r494 = new FlowerPart(this);
		cube_r494.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal33.addChild(cube_r494);
		setRotationAngle(cube_r494, 0.0F, 0.0F, 0.8814F);
		cube_r494.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r495 = new FlowerPart(this);
		cube_r495.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal33.addChild(cube_r495);
		setRotationAngle(cube_r495, 0.0F, 0.0F, 0.5498F);
		cube_r495.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r496 = new FlowerPart(this);
		cube_r496.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal33.addChild(cube_r496);
		setRotationAngle(cube_r496, 0.0F, 0.0F, 0.8814F);
		cube_r496.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal34 = new FlowerPart(this);
		stemal34.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal34);
		setRotationAngle(stemal34, -0.2949F, -0.1812F, -0.7237F);
		stemal34.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r497 = new FlowerPart(this);
		cube_r497.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal34.addChild(cube_r497);
		setRotationAngle(cube_r497, 0.0F, 0.0F, 0.8814F);
		cube_r497.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r498 = new FlowerPart(this);
		cube_r498.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal34.addChild(cube_r498);
		setRotationAngle(cube_r498, 0.0F, 0.0F, 0.2618F);
		cube_r498.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));
	}

	private void init84() {

		cube_r499 = new FlowerPart(this);
		cube_r499.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal34.addChild(cube_r499);
		setRotationAngle(cube_r499, 0.0F, 0.0F, 0.8814F);
		cube_r499.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r500 = new FlowerPart(this);
		cube_r500.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal34.addChild(cube_r500);
		setRotationAngle(cube_r500, 0.0F, 0.0F, 0.5498F);
		cube_r500.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r501 = new FlowerPart(this);
		cube_r501.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal34.addChild(cube_r501);
		setRotationAngle(cube_r501, 0.0F, 0.0F, 0.8814F);
		cube_r501.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal35 = new FlowerPart(this);
		stemal35.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal35);
		setRotationAngle(stemal35, 0.3149F, 0.1775F, -0.2915F);
		stemal35.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r502 = new FlowerPart(this);
		cube_r502.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal35.addChild(cube_r502);
		setRotationAngle(cube_r502, 0.0F, 0.0F, 0.8814F);
		cube_r502.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r503 = new FlowerPart(this);
		cube_r503.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal35.addChild(cube_r503);
		setRotationAngle(cube_r503, 0.0F, 0.0F, 0.2618F);
		cube_r503.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r504 = new FlowerPart(this);
		cube_r504.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal35.addChild(cube_r504);
	}

	private void init85() {
		setRotationAngle(cube_r504, 0.0F, 0.0F, 0.8814F);
		cube_r504.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r505 = new FlowerPart(this);
		cube_r505.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal35.addChild(cube_r505);
		setRotationAngle(cube_r505, 0.0F, 0.0F, 0.5498F);
		cube_r505.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r506 = new FlowerPart(this);
		cube_r506.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal35.addChild(cube_r506);
		setRotationAngle(cube_r506, 0.0F, 0.0F, 0.8814F);
		cube_r506.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal36 = new FlowerPart(this);
		stemal36.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth6.addChild(stemal36);
		setRotationAngle(stemal36, 0.0F, 0.0F, -0.9163F);
		stemal36.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r507 = new FlowerPart(this);
		cube_r507.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal36.addChild(cube_r507);
		setRotationAngle(cube_r507, 0.0F, 0.0F, 0.8814F);
		cube_r507.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r508 = new FlowerPart(this);
		cube_r508.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal36.addChild(cube_r508);
		setRotationAngle(cube_r508, 0.0F, 0.0F, 0.2618F);
		cube_r508.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r509 = new FlowerPart(this);
		cube_r509.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal36.addChild(cube_r509);
		setRotationAngle(cube_r509, 0.0F, 0.0F, 0.8814F);
		cube_r509.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r510 = new FlowerPart(this);
	}

	private void init86() {
		cube_r510.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal36.addChild(cube_r510);
		setRotationAngle(cube_r510, 0.0F, 0.0F, 0.5498F);
		cube_r510.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r511 = new FlowerPart(this);
		cube_r511.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal36.addChild(cube_r511);
		setRotationAngle(cube_r511, 0.0F, 0.0F, 0.8814F);
		cube_r511.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		half2 = new FlowerPart(this);
		half2.setRotationPoint(10.565F, -59.84F, 6.79F);
		flower.addChild(half2);
		

		perianth3 = new FlowerPart(this);
		perianth3.setRotationPoint(-1.93F, -2.0F, -0.915F);
		half2.addChild(perianth3);
		setRotationAngle(perianth3, -2.9739F, 0.2748F, -2.5667F);
		

		tepal13 = new FlowerPart(this);
		tepal13.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal13);
		setRotationAngle(tepal13, 0.0F, 0.0F, -0.6109F);
		

		cube_r512 = new FlowerPart(this);
		cube_r512.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal13.addChild(cube_r512);
		setRotationAngle(cube_r512, 0.0F, 0.0F, 0.2574F);
		cube_r512.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r513 = new FlowerPart(this);
		cube_r513.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal13.addChild(cube_r513);
		setRotationAngle(cube_r513, -0.0167F, 0.1453F, 0.7743F);
		cube_r513.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r514 = new FlowerPart(this);
	}

	private void init87() {
		cube_r514.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal13.addChild(cube_r514);
		setRotationAngle(cube_r514, -0.0167F, -0.1339F, 0.7789F);
		cube_r514.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r515 = new FlowerPart(this);
		cube_r515.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal13.addChild(cube_r515);
		setRotationAngle(cube_r515, -0.0167F, -0.1339F, 0.7789F);
		cube_r515.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r516 = new FlowerPart(this);
		cube_r516.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal13.addChild(cube_r516);
		setRotationAngle(cube_r516, -0.0165F, 0.0057F, 0.7766F);
		cube_r516.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r516.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r516.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r517 = new FlowerPart(this);
		cube_r517.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal13.addChild(cube_r517);
		setRotationAngle(cube_r517, -0.0139F, -0.0105F, -0.2006F);
		cube_r517.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r518 = new FlowerPart(this);
		cube_r518.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal13.addChild(cube_r518);
		setRotationAngle(cube_r518, 0.0015F, -0.0174F, -1.213F);
		cube_r518.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r519 = new FlowerPart(this);
		cube_r519.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal13.addChild(cube_r519);
		setRotationAngle(cube_r519, 0.0F, 0.1047F, -2.4696F);
		cube_r519.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r520 = new FlowerPart(this);
		cube_r520.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal13.addChild(cube_r520);
	}

	private void init88() {
		setRotationAngle(cube_r520, 0.0F, -0.1047F, -2.4696F);
		cube_r520.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r521 = new FlowerPart(this);
		cube_r521.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal13.addChild(cube_r521);
		setRotationAngle(cube_r521, 0.0F, 0.0F, -2.4696F);
		cube_r521.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r521.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r521.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r522 = new FlowerPart(this);
		cube_r522.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal13.addChild(cube_r522);
		setRotationAngle(cube_r522, 0.0F, 0.0F, 2.8536F);
		cube_r522.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r523 = new FlowerPart(this);
		cube_r523.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal13.addChild(cube_r523);
		setRotationAngle(cube_r523, 0.0F, -0.0873F, 1.8588F);
		cube_r523.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r524 = new FlowerPart(this);
		cube_r524.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal13.addChild(cube_r524);
		setRotationAngle(cube_r524, 0.0F, 0.1309F, 1.8588F);
		cube_r524.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r525 = new FlowerPart(this);
		cube_r525.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal13.addChild(cube_r525);
		setRotationAngle(cube_r525, 0.0F, 0.0F, 1.8588F);
		cube_r525.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r525.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r526 = new FlowerPart(this);
		cube_r526.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal13.addChild(cube_r526);
		setRotationAngle(cube_r526, 0.0F, 0.0F, 1.8588F);
	}

	private void init89() {
		cube_r526.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r527 = new FlowerPart(this);
		cube_r527.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal13.addChild(cube_r527);
		setRotationAngle(cube_r527, 0.0F, 0.0F, 1.0036F);
		cube_r527.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r528 = new FlowerPart(this);
		cube_r528.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal13.addChild(cube_r528);
		setRotationAngle(cube_r528, 0.0F, -0.0873F, 0.5236F);
		cube_r528.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r529 = new FlowerPart(this);
		cube_r529.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal13.addChild(cube_r529);
		setRotationAngle(cube_r529, 0.0F, 0.0873F, 0.5236F);
		cube_r529.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r530 = new FlowerPart(this);
		cube_r530.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal13.addChild(cube_r530);
		setRotationAngle(cube_r530, 0.0F, 0.0F, 0.5236F);
		cube_r530.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r530.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r531 = new FlowerPart(this);
		cube_r531.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal13.addChild(cube_r531);
		setRotationAngle(cube_r531, 0.0F, 0.0F, -0.1745F);
		cube_r531.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r532 = new FlowerPart(this);
		cube_r532.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal13.addChild(cube_r532);
		setRotationAngle(cube_r532, 0.0F, -0.2618F, -0.1745F);
		cube_r532.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r533 = new FlowerPart(this);
	}

	private void init90() {
		cube_r533.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal13.addChild(cube_r533);
		setRotationAngle(cube_r533, 0.0F, 0.2618F, -0.1745F);
		cube_r533.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r534 = new FlowerPart(this);
		cube_r534.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal13.addChild(cube_r534);
		setRotationAngle(cube_r534, 0.0F, 0.0F, -0.1745F);
		cube_r534.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal14 = new FlowerPart(this);
		tepal14.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal14);
		setRotationAngle(tepal14, -0.6319F, 0.9025F, -1.0123F);
		

		cube_r535 = new FlowerPart(this);
		cube_r535.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal14.addChild(cube_r535);
		setRotationAngle(cube_r535, 0.0F, 0.0F, 0.2574F);
		cube_r535.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r536 = new FlowerPart(this);
		cube_r536.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal14.addChild(cube_r536);
		setRotationAngle(cube_r536, -0.0167F, 0.1453F, 0.7743F);
		cube_r536.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r537 = new FlowerPart(this);
		cube_r537.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal14.addChild(cube_r537);
		setRotationAngle(cube_r537, -0.0167F, -0.1339F, 0.7789F);
		cube_r537.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r538 = new FlowerPart(this);
		cube_r538.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal14.addChild(cube_r538);
		setRotationAngle(cube_r538, -0.0167F, -0.1339F, 0.7789F);
		cube_r538.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init91() {

		cube_r539 = new FlowerPart(this);
		cube_r539.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal14.addChild(cube_r539);
		setRotationAngle(cube_r539, -0.0165F, 0.0057F, 0.7766F);
		cube_r539.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r539.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r539.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r540 = new FlowerPart(this);
		cube_r540.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal14.addChild(cube_r540);
		setRotationAngle(cube_r540, -0.0139F, -0.0105F, -0.2006F);
		cube_r540.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r541 = new FlowerPart(this);
		cube_r541.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal14.addChild(cube_r541);
		setRotationAngle(cube_r541, 0.0015F, -0.0174F, -1.213F);
		cube_r541.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r542 = new FlowerPart(this);
		cube_r542.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal14.addChild(cube_r542);
		setRotationAngle(cube_r542, 0.0F, 0.1047F, -2.4696F);
		cube_r542.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r543 = new FlowerPart(this);
		cube_r543.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal14.addChild(cube_r543);
		setRotationAngle(cube_r543, 0.0F, -0.1047F, -2.4696F);
		cube_r543.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r544 = new FlowerPart(this);
		cube_r544.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal14.addChild(cube_r544);
		setRotationAngle(cube_r544, 0.0F, 0.0F, -2.4696F);
		cube_r544.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r544.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r544.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));
	}

	private void init92() {

		cube_r545 = new FlowerPart(this);
		cube_r545.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal14.addChild(cube_r545);
		setRotationAngle(cube_r545, 0.0F, 0.0F, 2.8536F);
		cube_r545.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r546 = new FlowerPart(this);
		cube_r546.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal14.addChild(cube_r546);
		setRotationAngle(cube_r546, 0.0F, -0.0873F, 1.8588F);
		cube_r546.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r547 = new FlowerPart(this);
		cube_r547.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal14.addChild(cube_r547);
		setRotationAngle(cube_r547, 0.0F, 0.1309F, 1.8588F);
		cube_r547.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r548 = new FlowerPart(this);
		cube_r548.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal14.addChild(cube_r548);
		setRotationAngle(cube_r548, 0.0F, 0.0F, 1.8588F);
		cube_r548.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r548.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r549 = new FlowerPart(this);
		cube_r549.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal14.addChild(cube_r549);
		setRotationAngle(cube_r549, 0.0F, 0.0F, 1.8588F);
		cube_r549.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r550 = new FlowerPart(this);
		cube_r550.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal14.addChild(cube_r550);
		setRotationAngle(cube_r550, 0.0F, 0.0F, 1.0036F);
		cube_r550.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r551 = new FlowerPart(this);
		cube_r551.setRotationPoint(7.075F, -0.575F, -1.0F);
	}

	private void init93() {
		tepal14.addChild(cube_r551);
		setRotationAngle(cube_r551, 0.0F, -0.0873F, 0.5236F);
		cube_r551.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r552 = new FlowerPart(this);
		cube_r552.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal14.addChild(cube_r552);
		setRotationAngle(cube_r552, 0.0F, 0.0873F, 0.5236F);
		cube_r552.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r553 = new FlowerPart(this);
		cube_r553.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal14.addChild(cube_r553);
		setRotationAngle(cube_r553, 0.0F, 0.0F, 0.5236F);
		cube_r553.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r553.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r554 = new FlowerPart(this);
		cube_r554.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal14.addChild(cube_r554);
		setRotationAngle(cube_r554, 0.0F, 0.0F, -0.1745F);
		cube_r554.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r555 = new FlowerPart(this);
		cube_r555.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal14.addChild(cube_r555);
		setRotationAngle(cube_r555, 0.0F, -0.2618F, -0.1745F);
		cube_r555.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r556 = new FlowerPart(this);
		cube_r556.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal14.addChild(cube_r556);
		setRotationAngle(cube_r556, 0.0F, 0.2618F, -0.1745F);
		cube_r556.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r557 = new FlowerPart(this);
		cube_r557.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal14.addChild(cube_r557);
		setRotationAngle(cube_r557, 0.0F, 0.0F, -0.1745F);
		cube_r557.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));
	}

	private void init94() {

		tepal15 = new FlowerPart(this);
		tepal15.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal15);
		setRotationAngle(tepal15, -2.6068F, 0.9507F, -2.7742F);
		

		cube_r558 = new FlowerPart(this);
		cube_r558.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal15.addChild(cube_r558);
		setRotationAngle(cube_r558, 0.0F, 0.0F, 0.2574F);
		cube_r558.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r559 = new FlowerPart(this);
		cube_r559.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal15.addChild(cube_r559);
		setRotationAngle(cube_r559, -0.0167F, 0.1453F, 0.7743F);
		cube_r559.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r560 = new FlowerPart(this);
		cube_r560.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal15.addChild(cube_r560);
		setRotationAngle(cube_r560, -0.0167F, -0.1339F, 0.7789F);
		cube_r560.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r561 = new FlowerPart(this);
		cube_r561.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal15.addChild(cube_r561);
		setRotationAngle(cube_r561, -0.0167F, -0.1339F, 0.7789F);
		cube_r561.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r562 = new FlowerPart(this);
		cube_r562.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal15.addChild(cube_r562);
		setRotationAngle(cube_r562, -0.0165F, 0.0057F, 0.7766F);
		cube_r562.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r562.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r562.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r563 = new FlowerPart(this);
	}

	private void init95() {
		cube_r563.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal15.addChild(cube_r563);
		setRotationAngle(cube_r563, -0.0139F, -0.0105F, -0.2006F);
		cube_r563.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r564 = new FlowerPart(this);
		cube_r564.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal15.addChild(cube_r564);
		setRotationAngle(cube_r564, 0.0015F, -0.0174F, -1.213F);
		cube_r564.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r565 = new FlowerPart(this);
		cube_r565.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal15.addChild(cube_r565);
		setRotationAngle(cube_r565, 0.0F, 0.1047F, -2.4696F);
		cube_r565.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r566 = new FlowerPart(this);
		cube_r566.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal15.addChild(cube_r566);
		setRotationAngle(cube_r566, 0.0F, -0.1047F, -2.4696F);
		cube_r566.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r567 = new FlowerPart(this);
		cube_r567.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal15.addChild(cube_r567);
		setRotationAngle(cube_r567, 0.0F, 0.0F, -2.4696F);
		cube_r567.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r567.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r567.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r568 = new FlowerPart(this);
		cube_r568.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal15.addChild(cube_r568);
		setRotationAngle(cube_r568, 0.0F, 0.0F, 2.8536F);
		cube_r568.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r569 = new FlowerPart(this);
		cube_r569.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal15.addChild(cube_r569);
	}

	private void init96() {
		setRotationAngle(cube_r569, 0.0F, -0.0873F, 1.8588F);
		cube_r569.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r570 = new FlowerPart(this);
		cube_r570.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal15.addChild(cube_r570);
		setRotationAngle(cube_r570, 0.0F, 0.1309F, 1.8588F);
		cube_r570.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r571 = new FlowerPart(this);
		cube_r571.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal15.addChild(cube_r571);
		setRotationAngle(cube_r571, 0.0F, 0.0F, 1.8588F);
		cube_r571.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r571.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r572 = new FlowerPart(this);
		cube_r572.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal15.addChild(cube_r572);
		setRotationAngle(cube_r572, 0.0F, 0.0F, 1.8588F);
		cube_r572.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r573 = new FlowerPart(this);
		cube_r573.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal15.addChild(cube_r573);
		setRotationAngle(cube_r573, 0.0F, 0.0F, 1.0036F);
		cube_r573.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r574 = new FlowerPart(this);
		cube_r574.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal15.addChild(cube_r574);
		setRotationAngle(cube_r574, 0.0F, -0.0873F, 0.5236F);
		cube_r574.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r575 = new FlowerPart(this);
		cube_r575.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal15.addChild(cube_r575);
		setRotationAngle(cube_r575, 0.0F, 0.0873F, 0.5236F);
		cube_r575.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r576 = new FlowerPart(this);
	}

	private void init97() {
		cube_r576.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal15.addChild(cube_r576);
		setRotationAngle(cube_r576, 0.0F, 0.0F, 0.5236F);
		cube_r576.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r576.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r577 = new FlowerPart(this);
		cube_r577.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal15.addChild(cube_r577);
		setRotationAngle(cube_r577, 0.0F, 0.0F, -0.1745F);
		cube_r577.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r578 = new FlowerPart(this);
		cube_r578.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal15.addChild(cube_r578);
		setRotationAngle(cube_r578, 0.0F, -0.2618F, -0.1745F);
		cube_r578.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r579 = new FlowerPart(this);
		cube_r579.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal15.addChild(cube_r579);
		setRotationAngle(cube_r579, 0.0F, 0.2618F, -0.1745F);
		cube_r579.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r580 = new FlowerPart(this);
		cube_r580.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal15.addChild(cube_r580);
		setRotationAngle(cube_r580, 0.0F, 0.0F, -0.1745F);
		cube_r580.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal16 = new FlowerPart(this);
		tepal16.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal16);
		setRotationAngle(tepal16, -3.1416F, 0.0F, -3.0543F);
		

		cube_r581 = new FlowerPart(this);
		cube_r581.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal16.addChild(cube_r581);
		setRotationAngle(cube_r581, 0.0F, 0.0F, 0.2574F);
	}

	private void init98() {
		cube_r581.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r582 = new FlowerPart(this);
		cube_r582.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal16.addChild(cube_r582);
		setRotationAngle(cube_r582, -0.0167F, 0.1453F, 0.7743F);
		cube_r582.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r583 = new FlowerPart(this);
		cube_r583.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal16.addChild(cube_r583);
		setRotationAngle(cube_r583, -0.0167F, -0.1339F, 0.7789F);
		cube_r583.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r584 = new FlowerPart(this);
		cube_r584.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal16.addChild(cube_r584);
		setRotationAngle(cube_r584, -0.0167F, -0.1339F, 0.7789F);
		cube_r584.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r585 = new FlowerPart(this);
		cube_r585.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal16.addChild(cube_r585);
		setRotationAngle(cube_r585, -0.0165F, 0.0057F, 0.7766F);
		cube_r585.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r585.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r585.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r586 = new FlowerPart(this);
		cube_r586.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal16.addChild(cube_r586);
		setRotationAngle(cube_r586, -0.0139F, -0.0105F, -0.2006F);
		cube_r586.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r587 = new FlowerPart(this);
		cube_r587.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal16.addChild(cube_r587);
		setRotationAngle(cube_r587, 0.0015F, -0.0174F, -1.213F);
		cube_r587.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r588 = new FlowerPart(this);
	}

	private void init99() {
		cube_r588.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal16.addChild(cube_r588);
		setRotationAngle(cube_r588, 0.0F, 0.1047F, -2.4696F);
		cube_r588.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r589 = new FlowerPart(this);
		cube_r589.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal16.addChild(cube_r589);
		setRotationAngle(cube_r589, 0.0F, -0.1047F, -2.4696F);
		cube_r589.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r590 = new FlowerPart(this);
		cube_r590.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal16.addChild(cube_r590);
		setRotationAngle(cube_r590, 0.0F, 0.0F, -2.4696F);
		cube_r590.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r590.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r590.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r591 = new FlowerPart(this);
		cube_r591.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal16.addChild(cube_r591);
		setRotationAngle(cube_r591, 0.0F, 0.0F, 2.8536F);
		cube_r591.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r592 = new FlowerPart(this);
		cube_r592.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal16.addChild(cube_r592);
		setRotationAngle(cube_r592, 0.0F, -0.0873F, 1.8588F);
		cube_r592.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r593 = new FlowerPart(this);
		cube_r593.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal16.addChild(cube_r593);
		setRotationAngle(cube_r593, 0.0F, 0.1309F, 1.8588F);
		cube_r593.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r594 = new FlowerPart(this);
		cube_r594.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal16.addChild(cube_r594);
	}

	private void init100() {
		setRotationAngle(cube_r594, 0.0F, 0.0F, 1.8588F);
		cube_r594.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r594.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r595 = new FlowerPart(this);
		cube_r595.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal16.addChild(cube_r595);
		setRotationAngle(cube_r595, 0.0F, 0.0F, 1.8588F);
		cube_r595.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r596 = new FlowerPart(this);
		cube_r596.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal16.addChild(cube_r596);
		setRotationAngle(cube_r596, 0.0F, 0.0F, 1.0036F);
		cube_r596.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r597 = new FlowerPart(this);
		cube_r597.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal16.addChild(cube_r597);
		setRotationAngle(cube_r597, 0.0F, -0.0873F, 0.5236F);
		cube_r597.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r598 = new FlowerPart(this);
		cube_r598.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal16.addChild(cube_r598);
		setRotationAngle(cube_r598, 0.0F, 0.0873F, 0.5236F);
		cube_r598.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r599 = new FlowerPart(this);
		cube_r599.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal16.addChild(cube_r599);
		setRotationAngle(cube_r599, 0.0F, 0.0F, 0.5236F);
		cube_r599.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r599.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r600 = new FlowerPart(this);
		cube_r600.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal16.addChild(cube_r600);
		setRotationAngle(cube_r600, 0.0F, 0.0F, -0.1745F);
		cube_r600.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));
	}

	private void init101() {

		cube_r601 = new FlowerPart(this);
		cube_r601.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal16.addChild(cube_r601);
		setRotationAngle(cube_r601, 0.0F, -0.2618F, -0.1745F);
		cube_r601.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r602 = new FlowerPart(this);
		cube_r602.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal16.addChild(cube_r602);
		setRotationAngle(cube_r602, 0.0F, 0.2618F, -0.1745F);
		cube_r602.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r603 = new FlowerPart(this);
		cube_r603.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal16.addChild(cube_r603);
		setRotationAngle(cube_r603, 0.0F, 0.0F, -0.1745F);
		cube_r603.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal17 = new FlowerPart(this);
		tepal17.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal17);
		setRotationAngle(tepal17, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r604 = new FlowerPart(this);
		cube_r604.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal17.addChild(cube_r604);
		setRotationAngle(cube_r604, 0.0F, 0.0F, 0.2574F);
		cube_r604.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r605 = new FlowerPart(this);
		cube_r605.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal17.addChild(cube_r605);
		setRotationAngle(cube_r605, -0.0167F, 0.1453F, 0.7743F);
		cube_r605.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r606 = new FlowerPart(this);
		cube_r606.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal17.addChild(cube_r606);
	}

	private void init102() {
		setRotationAngle(cube_r606, -0.0167F, -0.1339F, 0.7789F);
		cube_r606.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r607 = new FlowerPart(this);
		cube_r607.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal17.addChild(cube_r607);
		setRotationAngle(cube_r607, -0.0167F, -0.1339F, 0.7789F);
		cube_r607.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r608 = new FlowerPart(this);
		cube_r608.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal17.addChild(cube_r608);
		setRotationAngle(cube_r608, -0.0165F, 0.0057F, 0.7766F);
		cube_r608.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r608.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r608.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r609 = new FlowerPart(this);
		cube_r609.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal17.addChild(cube_r609);
		setRotationAngle(cube_r609, -0.0139F, -0.0105F, -0.2006F);
		cube_r609.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r610 = new FlowerPart(this);
		cube_r610.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal17.addChild(cube_r610);
		setRotationAngle(cube_r610, 0.0015F, -0.0174F, -1.213F);
		cube_r610.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r611 = new FlowerPart(this);
		cube_r611.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal17.addChild(cube_r611);
		setRotationAngle(cube_r611, 0.0F, 0.1047F, -2.4696F);
		cube_r611.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r612 = new FlowerPart(this);
		cube_r612.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal17.addChild(cube_r612);
		setRotationAngle(cube_r612, 0.0F, -0.1047F, -2.4696F);
		cube_r612.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init103() {

		cube_r613 = new FlowerPart(this);
		cube_r613.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal17.addChild(cube_r613);
		setRotationAngle(cube_r613, 0.0F, 0.0F, -2.4696F);
		cube_r613.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r613.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r613.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r614 = new FlowerPart(this);
		cube_r614.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal17.addChild(cube_r614);
		setRotationAngle(cube_r614, 0.0F, 0.0F, 2.8536F);
		cube_r614.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r615 = new FlowerPart(this);
		cube_r615.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal17.addChild(cube_r615);
		setRotationAngle(cube_r615, 0.0F, -0.0873F, 1.8588F);
		cube_r615.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r616 = new FlowerPart(this);
		cube_r616.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal17.addChild(cube_r616);
		setRotationAngle(cube_r616, 0.0F, 0.1309F, 1.8588F);
		cube_r616.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r617 = new FlowerPart(this);
		cube_r617.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal17.addChild(cube_r617);
		setRotationAngle(cube_r617, 0.0F, 0.0F, 1.8588F);
		cube_r617.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r617.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r618 = new FlowerPart(this);
		cube_r618.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal17.addChild(cube_r618);
		setRotationAngle(cube_r618, 0.0F, 0.0F, 1.8588F);
		cube_r618.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r619 = new FlowerPart(this);
	}

	private void init104() {
		cube_r619.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal17.addChild(cube_r619);
		setRotationAngle(cube_r619, 0.0F, 0.0F, 1.0036F);
		cube_r619.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r620 = new FlowerPart(this);
		cube_r620.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal17.addChild(cube_r620);
		setRotationAngle(cube_r620, 0.0F, -0.0873F, 0.5236F);
		cube_r620.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r621 = new FlowerPart(this);
		cube_r621.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal17.addChild(cube_r621);
		setRotationAngle(cube_r621, 0.0F, 0.0873F, 0.5236F);
		cube_r621.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r622 = new FlowerPart(this);
		cube_r622.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal17.addChild(cube_r622);
		setRotationAngle(cube_r622, 0.0F, 0.0F, 0.5236F);
		cube_r622.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r622.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r623 = new FlowerPart(this);
		cube_r623.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal17.addChild(cube_r623);
		setRotationAngle(cube_r623, 0.0F, 0.0F, -0.1745F);
		cube_r623.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r624 = new FlowerPart(this);
		cube_r624.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal17.addChild(cube_r624);
		setRotationAngle(cube_r624, 0.0F, -0.2618F, -0.1745F);
		cube_r624.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r625 = new FlowerPart(this);
		cube_r625.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal17.addChild(cube_r625);
		setRotationAngle(cube_r625, 0.0F, 0.2618F, -0.1745F);
	}

	private void init105() {
		cube_r625.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r626 = new FlowerPart(this);
		cube_r626.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal17.addChild(cube_r626);
		setRotationAngle(cube_r626, 0.0F, 0.0F, -0.1745F);
		cube_r626.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal18 = new FlowerPart(this);
		tepal18.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth3.addChild(tepal18);
		setRotationAngle(tepal18, 0.5348F, -0.9507F, -0.891F);
		

		cube_r627 = new FlowerPart(this);
		cube_r627.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal18.addChild(cube_r627);
		setRotationAngle(cube_r627, 0.0F, 0.0F, 0.2574F);
		cube_r627.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r628 = new FlowerPart(this);
		cube_r628.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal18.addChild(cube_r628);
		setRotationAngle(cube_r628, -0.0167F, 0.1453F, 0.7743F);
		cube_r628.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r629 = new FlowerPart(this);
		cube_r629.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal18.addChild(cube_r629);
		setRotationAngle(cube_r629, -0.0167F, -0.1339F, 0.7789F);
		cube_r629.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r630 = new FlowerPart(this);
		cube_r630.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal18.addChild(cube_r630);
		setRotationAngle(cube_r630, -0.0167F, -0.1339F, 0.7789F);
		cube_r630.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r631 = new FlowerPart(this);
		cube_r631.setRotationPoint(8.89F, 3.21F, -0.15F);
	}

	private void init106() {
		tepal18.addChild(cube_r631);
		setRotationAngle(cube_r631, -0.0165F, 0.0057F, 0.7766F);
		cube_r631.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r631.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r631.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r632 = new FlowerPart(this);
		cube_r632.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal18.addChild(cube_r632);
		setRotationAngle(cube_r632, -0.0139F, -0.0105F, -0.2006F);
		cube_r632.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r633 = new FlowerPart(this);
		cube_r633.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal18.addChild(cube_r633);
		setRotationAngle(cube_r633, 0.0015F, -0.0174F, -1.213F);
		cube_r633.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r634 = new FlowerPart(this);
		cube_r634.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal18.addChild(cube_r634);
		setRotationAngle(cube_r634, 0.0F, 0.1047F, -2.4696F);
		cube_r634.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r635 = new FlowerPart(this);
		cube_r635.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal18.addChild(cube_r635);
		setRotationAngle(cube_r635, 0.0F, -0.1047F, -2.4696F);
		cube_r635.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r636 = new FlowerPart(this);
		cube_r636.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal18.addChild(cube_r636);
		setRotationAngle(cube_r636, 0.0F, 0.0F, -2.4696F);
		cube_r636.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r636.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r636.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r637 = new FlowerPart(this);
		cube_r637.setRotationPoint(11.035F, 5.03F, 0.0F);
	}

	private void init107() {
		tepal18.addChild(cube_r637);
		setRotationAngle(cube_r637, 0.0F, 0.0F, 2.8536F);
		cube_r637.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r638 = new FlowerPart(this);
		cube_r638.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal18.addChild(cube_r638);
		setRotationAngle(cube_r638, 0.0F, -0.0873F, 1.8588F);
		cube_r638.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r639 = new FlowerPart(this);
		cube_r639.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal18.addChild(cube_r639);
		setRotationAngle(cube_r639, 0.0F, 0.1309F, 1.8588F);
		cube_r639.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r640 = new FlowerPart(this);
		cube_r640.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal18.addChild(cube_r640);
		setRotationAngle(cube_r640, 0.0F, 0.0F, 1.8588F);
		cube_r640.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r640.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r641 = new FlowerPart(this);
		cube_r641.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal18.addChild(cube_r641);
		setRotationAngle(cube_r641, 0.0F, 0.0F, 1.8588F);
		cube_r641.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r642 = new FlowerPart(this);
		cube_r642.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal18.addChild(cube_r642);
		setRotationAngle(cube_r642, 0.0F, 0.0F, 1.0036F);
		cube_r642.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r643 = new FlowerPart(this);
		cube_r643.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal18.addChild(cube_r643);
		setRotationAngle(cube_r643, 0.0F, -0.0873F, 0.5236F);
		cube_r643.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init108() {

		cube_r644 = new FlowerPart(this);
		cube_r644.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal18.addChild(cube_r644);
		setRotationAngle(cube_r644, 0.0F, 0.0873F, 0.5236F);
		cube_r644.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r645 = new FlowerPart(this);
		cube_r645.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal18.addChild(cube_r645);
		setRotationAngle(cube_r645, 0.0F, 0.0F, 0.5236F);
		cube_r645.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r645.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r646 = new FlowerPart(this);
		cube_r646.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal18.addChild(cube_r646);
		setRotationAngle(cube_r646, 0.0F, 0.0F, -0.1745F);
		cube_r646.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r647 = new FlowerPart(this);
		cube_r647.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal18.addChild(cube_r647);
		setRotationAngle(cube_r647, 0.0F, -0.2618F, -0.1745F);
		cube_r647.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r648 = new FlowerPart(this);
		cube_r648.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal18.addChild(cube_r648);
		setRotationAngle(cube_r648, 0.0F, 0.2618F, -0.1745F);
		cube_r648.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r649 = new FlowerPart(this);
		cube_r649.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal18.addChild(cube_r649);
		setRotationAngle(cube_r649, 0.0F, 0.0F, -0.1745F);
		cube_r649.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal13 = new FlowerPart(this);
		stemal13.setRotationPoint(0.0F, -1.0F, 0.0F);
	}

	private void init109() {
		perianth3.addChild(stemal13);
		setRotationAngle(stemal13, -0.4931F, -0.438F, -0.0571F);
		stemal13.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r650 = new FlowerPart(this);
		cube_r650.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal13.addChild(cube_r650);
		setRotationAngle(cube_r650, 0.0F, 0.0F, 0.8814F);
		cube_r650.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r651 = new FlowerPart(this);
		cube_r651.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal13.addChild(cube_r651);
		setRotationAngle(cube_r651, 0.0F, 0.0F, 0.2618F);
		cube_r651.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r652 = new FlowerPart(this);
		cube_r652.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal13.addChild(cube_r652);
		setRotationAngle(cube_r652, 0.0F, 0.0F, 0.8814F);
		cube_r652.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r653 = new FlowerPart(this);
		cube_r653.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal13.addChild(cube_r653);
		setRotationAngle(cube_r653, 0.0F, 0.0F, 0.5498F);
		cube_r653.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r654 = new FlowerPart(this);
		cube_r654.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal13.addChild(cube_r654);
		setRotationAngle(cube_r654, 0.0F, 0.0F, 0.8814F);
		cube_r654.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal14 = new FlowerPart(this);
		stemal14.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth3.addChild(stemal14);
		setRotationAngle(stemal14, -0.1017F, 0.0303F, 0.0497F);
		stemal14.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r655 = new FlowerPart(this);
	}

	private void init110() {
		cube_r655.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal14.addChild(cube_r655);
		setRotationAngle(cube_r655, 0.0F, 0.0F, 0.8814F);
		cube_r655.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r656 = new FlowerPart(this);
		cube_r656.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal14.addChild(cube_r656);
		setRotationAngle(cube_r656, 0.0F, 0.0F, 0.2618F);
		cube_r656.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r657 = new FlowerPart(this);
		cube_r657.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal14.addChild(cube_r657);
		setRotationAngle(cube_r657, 0.0F, 0.0F, 0.8814F);
		cube_r657.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r658 = new FlowerPart(this);
		cube_r658.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal14.addChild(cube_r658);
		setRotationAngle(cube_r658, 0.0F, 0.0F, 0.5498F);
		cube_r658.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r659 = new FlowerPart(this);
		cube_r659.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal14.addChild(cube_r659);
		setRotationAngle(cube_r659, 0.0F, 0.0F, 0.8814F);
		cube_r659.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal15 = new FlowerPart(this);
		stemal15.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth3.addChild(stemal15);
		setRotationAngle(stemal15, 0.1582F, 0.2804F, -0.7053F);
		stemal15.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r660 = new FlowerPart(this);
		cube_r660.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal15.addChild(cube_r660);
		setRotationAngle(cube_r660, 0.0F, 0.0F, 0.8814F);
		cube_r660.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));
	}

	private void init111() {

		cube_r661 = new FlowerPart(this);
		cube_r661.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal15.addChild(cube_r661);
		setRotationAngle(cube_r661, 0.0F, 0.0F, 0.2618F);
		cube_r661.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r662 = new FlowerPart(this);
		cube_r662.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal15.addChild(cube_r662);
		setRotationAngle(cube_r662, 0.0F, 0.0F, 0.8814F);
		cube_r662.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r663 = new FlowerPart(this);
		cube_r663.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal15.addChild(cube_r663);
		setRotationAngle(cube_r663, 0.0F, 0.0F, 0.5498F);
		cube_r663.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r664 = new FlowerPart(this);
		cube_r664.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal15.addChild(cube_r664);
		setRotationAngle(cube_r664, 0.0F, 0.0F, 0.8814F);
		cube_r664.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal16 = new FlowerPart(this);
		stemal16.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth3.addChild(stemal16);
		setRotationAngle(stemal16, -0.2949F, -0.1812F, -0.7237F);
		stemal16.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r665 = new FlowerPart(this);
		cube_r665.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal16.addChild(cube_r665);
		setRotationAngle(cube_r665, 0.0F, 0.0F, 0.8814F);
		cube_r665.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r666 = new FlowerPart(this);
		cube_r666.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal16.addChild(cube_r666);
	}

	private void init112() {
		setRotationAngle(cube_r666, 0.0F, 0.0F, 0.2618F);
		cube_r666.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r667 = new FlowerPart(this);
		cube_r667.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal16.addChild(cube_r667);
		setRotationAngle(cube_r667, 0.0F, 0.0F, 0.8814F);
		cube_r667.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r668 = new FlowerPart(this);
		cube_r668.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal16.addChild(cube_r668);
		setRotationAngle(cube_r668, 0.0F, 0.0F, 0.5498F);
		cube_r668.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r669 = new FlowerPart(this);
		cube_r669.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal16.addChild(cube_r669);
		setRotationAngle(cube_r669, 0.0F, 0.0F, 0.8814F);
		cube_r669.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal17 = new FlowerPart(this);
		stemal17.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth3.addChild(stemal17);
		setRotationAngle(stemal17, 0.3149F, 0.1775F, -0.2915F);
		stemal17.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r670 = new FlowerPart(this);
		cube_r670.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal17.addChild(cube_r670);
		setRotationAngle(cube_r670, 0.0F, 0.0F, 0.8814F);
		cube_r670.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r671 = new FlowerPart(this);
		cube_r671.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal17.addChild(cube_r671);
		setRotationAngle(cube_r671, 0.0F, 0.0F, 0.2618F);
		cube_r671.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r672 = new FlowerPart(this);
	}

	private void init113() {
		cube_r672.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal17.addChild(cube_r672);
		setRotationAngle(cube_r672, 0.0F, 0.0F, 0.8814F);
		cube_r672.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r673 = new FlowerPart(this);
		cube_r673.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal17.addChild(cube_r673);
		setRotationAngle(cube_r673, 0.0F, 0.0F, 0.5498F);
		cube_r673.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r674 = new FlowerPart(this);
		cube_r674.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal17.addChild(cube_r674);
		setRotationAngle(cube_r674, 0.0F, 0.0F, 0.8814F);
		cube_r674.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal18 = new FlowerPart(this);
		stemal18.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth3.addChild(stemal18);
		setRotationAngle(stemal18, 0.0F, 0.0F, -0.9163F);
		stemal18.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r675 = new FlowerPart(this);
		cube_r675.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal18.addChild(cube_r675);
		setRotationAngle(cube_r675, 0.0F, 0.0F, 0.8814F);
		cube_r675.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r676 = new FlowerPart(this);
		cube_r676.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal18.addChild(cube_r676);
		setRotationAngle(cube_r676, 0.0F, 0.0F, 0.2618F);
		cube_r676.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r677 = new FlowerPart(this);
		cube_r677.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal18.addChild(cube_r677);
		setRotationAngle(cube_r677, 0.0F, 0.0F, 0.8814F);
		cube_r677.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));
	}

	private void init114() {

		cube_r678 = new FlowerPart(this);
		cube_r678.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal18.addChild(cube_r678);
		setRotationAngle(cube_r678, 0.0F, 0.0F, 0.5498F);
		cube_r678.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r679 = new FlowerPart(this);
		cube_r679.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal18.addChild(cube_r679);
		setRotationAngle(cube_r679, 0.0F, 0.0F, 0.8814F);
		cube_r679.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		perianth2 = new FlowerPart(this);
		perianth2.setRotationPoint(-11.165F, -0.075F, 5.445F);
		half2.addChild(perianth2);
		setRotationAngle(perianth2, -1.9326F, 0.9246F, -1.9402F);
		

		tepal7 = new FlowerPart(this);
		tepal7.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth2.addChild(tepal7);
		setRotationAngle(tepal7, 0.0F, 0.0F, -0.6981F);
		

		cube_r680 = new FlowerPart(this);
		cube_r680.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal7.addChild(cube_r680);
		setRotationAngle(cube_r680, 0.0F, 0.0F, 0.2574F);
		cube_r680.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r681 = new FlowerPart(this);
		cube_r681.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal7.addChild(cube_r681);
		setRotationAngle(cube_r681, -0.0167F, 0.1453F, 0.7743F);
		cube_r681.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r682 = new FlowerPart(this);
		cube_r682.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal7.addChild(cube_r682);
	}

	private void init115() {
		setRotationAngle(cube_r682, -0.0167F, -0.1339F, 0.7789F);
		cube_r682.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r683 = new FlowerPart(this);
		cube_r683.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal7.addChild(cube_r683);
		setRotationAngle(cube_r683, -0.0167F, -0.1339F, 0.7789F);
		cube_r683.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r684 = new FlowerPart(this);
		cube_r684.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal7.addChild(cube_r684);
		setRotationAngle(cube_r684, -0.0165F, 0.0057F, 0.7766F);
		cube_r684.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r684.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r684.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r685 = new FlowerPart(this);
		cube_r685.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal7.addChild(cube_r685);
		setRotationAngle(cube_r685, -0.0139F, -0.0105F, -0.2006F);
		cube_r685.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r686 = new FlowerPart(this);
		cube_r686.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal7.addChild(cube_r686);
		setRotationAngle(cube_r686, 0.0015F, -0.0174F, -1.213F);
		cube_r686.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r687 = new FlowerPart(this);
		cube_r687.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal7.addChild(cube_r687);
		setRotationAngle(cube_r687, 0.0F, 0.1047F, -2.4696F);
		cube_r687.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r688 = new FlowerPart(this);
		cube_r688.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal7.addChild(cube_r688);
		setRotationAngle(cube_r688, 0.0F, -0.1047F, -2.4696F);
		cube_r688.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init116() {

		cube_r689 = new FlowerPart(this);
		cube_r689.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal7.addChild(cube_r689);
		setRotationAngle(cube_r689, 0.0F, 0.0F, -2.4696F);
		cube_r689.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r689.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r689.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r690 = new FlowerPart(this);
		cube_r690.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal7.addChild(cube_r690);
		setRotationAngle(cube_r690, 0.0F, 0.0F, 2.8536F);
		cube_r690.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r691 = new FlowerPart(this);
		cube_r691.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal7.addChild(cube_r691);
		setRotationAngle(cube_r691, 0.0F, -0.0873F, 1.8588F);
		cube_r691.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r692 = new FlowerPart(this);
		cube_r692.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal7.addChild(cube_r692);
		setRotationAngle(cube_r692, 0.0F, 0.1309F, 1.8588F);
		cube_r692.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r693 = new FlowerPart(this);
		cube_r693.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal7.addChild(cube_r693);
		setRotationAngle(cube_r693, 0.0F, 0.0F, 1.8588F);
		cube_r693.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r693.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r694 = new FlowerPart(this);
		cube_r694.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal7.addChild(cube_r694);
		setRotationAngle(cube_r694, 0.0F, 0.0F, 1.8588F);
		cube_r694.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r695 = new FlowerPart(this);
	}

	private void init117() {
		cube_r695.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal7.addChild(cube_r695);
		setRotationAngle(cube_r695, 0.0F, 0.0F, 1.0036F);
		cube_r695.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r696 = new FlowerPart(this);
		cube_r696.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal7.addChild(cube_r696);
		setRotationAngle(cube_r696, 0.0F, -0.0873F, 0.5236F);
		cube_r696.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r697 = new FlowerPart(this);
		cube_r697.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal7.addChild(cube_r697);
		setRotationAngle(cube_r697, 0.0F, 0.0873F, 0.5236F);
		cube_r697.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r698 = new FlowerPart(this);
		cube_r698.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal7.addChild(cube_r698);
		setRotationAngle(cube_r698, 0.0F, 0.0F, 0.5236F);
		cube_r698.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r698.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r699 = new FlowerPart(this);
		cube_r699.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal7.addChild(cube_r699);
		setRotationAngle(cube_r699, 0.0F, 0.0F, -0.1745F);
		cube_r699.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r700 = new FlowerPart(this);
		cube_r700.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal7.addChild(cube_r700);
		setRotationAngle(cube_r700, 0.0F, -0.2618F, -0.1745F);
		cube_r700.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r701 = new FlowerPart(this);
		cube_r701.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal7.addChild(cube_r701);
		setRotationAngle(cube_r701, 0.0F, 0.2618F, -0.1745F);
	}

	private void init118() {
		cube_r701.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r702 = new FlowerPart(this);
		cube_r702.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal7.addChild(cube_r702);
		setRotationAngle(cube_r702, 0.0F, 0.0F, -0.1745F);
		cube_r702.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal8 = new FlowerPart(this);
		tepal8.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth2.addChild(tepal8);
		setRotationAngle(tepal8, -0.4799F, 0.8743F, -0.8217F);
		

		cube_r703 = new FlowerPart(this);
		cube_r703.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal8.addChild(cube_r703);
		setRotationAngle(cube_r703, 0.0F, 0.0F, 0.2574F);
		cube_r703.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r704 = new FlowerPart(this);
		cube_r704.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal8.addChild(cube_r704);
		setRotationAngle(cube_r704, -0.0167F, 0.1453F, 0.7743F);
		cube_r704.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r705 = new FlowerPart(this);
		cube_r705.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal8.addChild(cube_r705);
		setRotationAngle(cube_r705, -0.0167F, -0.1339F, 0.7789F);
		cube_r705.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r706 = new FlowerPart(this);
		cube_r706.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal8.addChild(cube_r706);
		setRotationAngle(cube_r706, -0.0167F, -0.1339F, 0.7789F);
		cube_r706.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r707 = new FlowerPart(this);
		cube_r707.setRotationPoint(8.89F, 3.21F, -0.15F);
	}

	private void init119() {
		tepal8.addChild(cube_r707);
		setRotationAngle(cube_r707, -0.0165F, 0.0057F, 0.7766F);
		cube_r707.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r707.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r707.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r708 = new FlowerPart(this);
		cube_r708.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal8.addChild(cube_r708);
		setRotationAngle(cube_r708, -0.0139F, -0.0105F, -0.2006F);
		cube_r708.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r709 = new FlowerPart(this);
		cube_r709.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal8.addChild(cube_r709);
		setRotationAngle(cube_r709, 0.0015F, -0.0174F, -1.213F);
		cube_r709.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r710 = new FlowerPart(this);
		cube_r710.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal8.addChild(cube_r710);
		setRotationAngle(cube_r710, 0.0F, 0.1047F, -2.4696F);
		cube_r710.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r711 = new FlowerPart(this);
		cube_r711.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal8.addChild(cube_r711);
		setRotationAngle(cube_r711, 0.0F, -0.1047F, -2.4696F);
		cube_r711.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r712 = new FlowerPart(this);
		cube_r712.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal8.addChild(cube_r712);
		setRotationAngle(cube_r712, 0.0F, 0.0F, -2.4696F);
		cube_r712.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r712.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r712.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r713 = new FlowerPart(this);
		cube_r713.setRotationPoint(11.035F, 5.03F, 0.0F);
	}

	private void init120() {
		tepal8.addChild(cube_r713);
		setRotationAngle(cube_r713, 0.0F, 0.0F, 2.8536F);
		cube_r713.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r714 = new FlowerPart(this);
		cube_r714.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal8.addChild(cube_r714);
		setRotationAngle(cube_r714, 0.0F, -0.0873F, 1.8588F);
		cube_r714.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r715 = new FlowerPart(this);
		cube_r715.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal8.addChild(cube_r715);
		setRotationAngle(cube_r715, 0.0F, 0.1309F, 1.8588F);
		cube_r715.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r716 = new FlowerPart(this);
		cube_r716.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal8.addChild(cube_r716);
		setRotationAngle(cube_r716, 0.0F, 0.0F, 1.8588F);
		cube_r716.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r716.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r717 = new FlowerPart(this);
		cube_r717.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal8.addChild(cube_r717);
		setRotationAngle(cube_r717, 0.0F, 0.0F, 1.8588F);
		cube_r717.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r718 = new FlowerPart(this);
		cube_r718.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal8.addChild(cube_r718);
		setRotationAngle(cube_r718, 0.0F, 0.0F, 1.0036F);
		cube_r718.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r719 = new FlowerPart(this);
		cube_r719.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal8.addChild(cube_r719);
		setRotationAngle(cube_r719, 0.0F, -0.0873F, 0.5236F);
		cube_r719.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init121() {

		cube_r720 = new FlowerPart(this);
		cube_r720.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal8.addChild(cube_r720);
		setRotationAngle(cube_r720, 0.0F, 0.0873F, 0.5236F);
		cube_r720.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r721 = new FlowerPart(this);
		cube_r721.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal8.addChild(cube_r721);
		setRotationAngle(cube_r721, 0.0F, 0.0F, 0.5236F);
		cube_r721.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r721.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r722 = new FlowerPart(this);
		cube_r722.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal8.addChild(cube_r722);
		setRotationAngle(cube_r722, 0.0F, 0.0F, -0.1745F);
		cube_r722.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r723 = new FlowerPart(this);
		cube_r723.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal8.addChild(cube_r723);
		setRotationAngle(cube_r723, 0.0F, -0.2618F, -0.1745F);
		cube_r723.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r724 = new FlowerPart(this);
		cube_r724.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal8.addChild(cube_r724);
		setRotationAngle(cube_r724, 0.0F, 0.2618F, -0.1745F);
		cube_r724.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r725 = new FlowerPart(this);
		cube_r725.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal8.addChild(cube_r725);
		setRotationAngle(cube_r725, 0.0F, 0.0F, -0.1745F);
		cube_r725.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal9 = new FlowerPart(this);
		tepal9.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	private void init122() {
		perianth2.addChild(tepal9);
		setRotationAngle(tepal9, -2.6068F, 0.9507F, -2.7742F);
		

		cube_r726 = new FlowerPart(this);
		cube_r726.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal9.addChild(cube_r726);
		setRotationAngle(cube_r726, 0.0F, 0.0F, 0.2574F);
		cube_r726.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r727 = new FlowerPart(this);
		cube_r727.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal9.addChild(cube_r727);
		setRotationAngle(cube_r727, -0.0167F, 0.1453F, 0.7743F);
		cube_r727.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r728 = new FlowerPart(this);
		cube_r728.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal9.addChild(cube_r728);
		setRotationAngle(cube_r728, -0.0167F, -0.1339F, 0.7789F);
		cube_r728.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r729 = new FlowerPart(this);
		cube_r729.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal9.addChild(cube_r729);
		setRotationAngle(cube_r729, -0.0167F, -0.1339F, 0.7789F);
		cube_r729.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r730 = new FlowerPart(this);
		cube_r730.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal9.addChild(cube_r730);
		setRotationAngle(cube_r730, -0.0165F, 0.0057F, 0.7766F);
		cube_r730.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r730.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r730.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r731 = new FlowerPart(this);
		cube_r731.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal9.addChild(cube_r731);
		setRotationAngle(cube_r731, -0.0139F, -0.0105F, -0.2006F);
	}

	private void init123() {
		cube_r731.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r732 = new FlowerPart(this);
		cube_r732.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal9.addChild(cube_r732);
		setRotationAngle(cube_r732, 0.0015F, -0.0174F, -1.213F);
		cube_r732.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r733 = new FlowerPart(this);
		cube_r733.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal9.addChild(cube_r733);
		setRotationAngle(cube_r733, 0.0F, 0.1047F, -2.4696F);
		cube_r733.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r734 = new FlowerPart(this);
		cube_r734.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal9.addChild(cube_r734);
		setRotationAngle(cube_r734, 0.0F, -0.1047F, -2.4696F);
		cube_r734.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r735 = new FlowerPart(this);
		cube_r735.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal9.addChild(cube_r735);
		setRotationAngle(cube_r735, 0.0F, 0.0F, -2.4696F);
		cube_r735.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r735.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r735.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r736 = new FlowerPart(this);
		cube_r736.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal9.addChild(cube_r736);
		setRotationAngle(cube_r736, 0.0F, 0.0F, 2.8536F);
		cube_r736.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r737 = new FlowerPart(this);
		cube_r737.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal9.addChild(cube_r737);
		setRotationAngle(cube_r737, 0.0F, -0.0873F, 1.8588F);
		cube_r737.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r738 = new FlowerPart(this);
	}

	private void init124() {
		cube_r738.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal9.addChild(cube_r738);
		setRotationAngle(cube_r738, 0.0F, 0.1309F, 1.8588F);
		cube_r738.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r739 = new FlowerPart(this);
		cube_r739.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal9.addChild(cube_r739);
		setRotationAngle(cube_r739, 0.0F, 0.0F, 1.8588F);
		cube_r739.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r739.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r740 = new FlowerPart(this);
		cube_r740.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal9.addChild(cube_r740);
		setRotationAngle(cube_r740, 0.0F, 0.0F, 1.8588F);
		cube_r740.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r741 = new FlowerPart(this);
		cube_r741.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal9.addChild(cube_r741);
		setRotationAngle(cube_r741, 0.0F, 0.0F, 1.0036F);
		cube_r741.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r742 = new FlowerPart(this);
		cube_r742.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal9.addChild(cube_r742);
		setRotationAngle(cube_r742, 0.0F, -0.0873F, 0.5236F);
		cube_r742.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r743 = new FlowerPart(this);
		cube_r743.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal9.addChild(cube_r743);
		setRotationAngle(cube_r743, 0.0F, 0.0873F, 0.5236F);
		cube_r743.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r744 = new FlowerPart(this);
		cube_r744.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal9.addChild(cube_r744);
		setRotationAngle(cube_r744, 0.0F, 0.0F, 0.5236F);
	}

	private void init125() {
		cube_r744.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r744.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r745 = new FlowerPart(this);
		cube_r745.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal9.addChild(cube_r745);
		setRotationAngle(cube_r745, 0.0F, 0.0F, -0.1745F);
		cube_r745.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r746 = new FlowerPart(this);
		cube_r746.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal9.addChild(cube_r746);
		setRotationAngle(cube_r746, 0.0F, -0.2618F, -0.1745F);
		cube_r746.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r747 = new FlowerPart(this);
		cube_r747.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal9.addChild(cube_r747);
		setRotationAngle(cube_r747, 0.0F, 0.2618F, -0.1745F);
		cube_r747.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r748 = new FlowerPart(this);
		cube_r748.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal9.addChild(cube_r748);
		setRotationAngle(cube_r748, 0.0F, 0.0F, -0.1745F);
		cube_r748.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal10 = new FlowerPart(this);
		tepal10.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth2.addChild(tepal10);
		setRotationAngle(tepal10, -3.1416F, 0.0F, -3.0543F);
		

		cube_r749 = new FlowerPart(this);
		cube_r749.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal10.addChild(cube_r749);
		setRotationAngle(cube_r749, 0.0F, 0.0F, 0.2574F);
		cube_r749.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r750 = new FlowerPart(this);
	}

	private void init126() {
		cube_r750.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal10.addChild(cube_r750);
		setRotationAngle(cube_r750, -0.0167F, 0.1453F, 0.7743F);
		cube_r750.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r751 = new FlowerPart(this);
		cube_r751.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal10.addChild(cube_r751);
		setRotationAngle(cube_r751, -0.0167F, -0.1339F, 0.7789F);
		cube_r751.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r752 = new FlowerPart(this);
		cube_r752.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal10.addChild(cube_r752);
		setRotationAngle(cube_r752, -0.0167F, -0.1339F, 0.7789F);
		cube_r752.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r753 = new FlowerPart(this);
		cube_r753.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal10.addChild(cube_r753);
		setRotationAngle(cube_r753, -0.0165F, 0.0057F, 0.7766F);
		cube_r753.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r753.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r753.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r754 = new FlowerPart(this);
		cube_r754.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal10.addChild(cube_r754);
		setRotationAngle(cube_r754, -0.0139F, -0.0105F, -0.2006F);
		cube_r754.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r755 = new FlowerPart(this);
		cube_r755.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal10.addChild(cube_r755);
		setRotationAngle(cube_r755, 0.0015F, -0.0174F, -1.213F);
		cube_r755.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r756 = new FlowerPart(this);
		cube_r756.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal10.addChild(cube_r756);
	}

	private void init127() {
		setRotationAngle(cube_r756, 0.0F, 0.1047F, -2.4696F);
		cube_r756.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r757 = new FlowerPart(this);
		cube_r757.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal10.addChild(cube_r757);
		setRotationAngle(cube_r757, 0.0F, -0.1047F, -2.4696F);
		cube_r757.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r758 = new FlowerPart(this);
		cube_r758.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal10.addChild(cube_r758);
		setRotationAngle(cube_r758, 0.0F, 0.0F, -2.4696F);
		cube_r758.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r758.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r758.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r759 = new FlowerPart(this);
		cube_r759.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal10.addChild(cube_r759);
		setRotationAngle(cube_r759, 0.0F, 0.0F, 2.8536F);
		cube_r759.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r760 = new FlowerPart(this);
		cube_r760.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal10.addChild(cube_r760);
		setRotationAngle(cube_r760, 0.0F, -0.0873F, 1.8588F);
		cube_r760.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r761 = new FlowerPart(this);
		cube_r761.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal10.addChild(cube_r761);
		setRotationAngle(cube_r761, 0.0F, 0.1309F, 1.8588F);
		cube_r761.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r762 = new FlowerPart(this);
		cube_r762.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal10.addChild(cube_r762);
		setRotationAngle(cube_r762, 0.0F, 0.0F, 1.8588F);
		cube_r762.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
	}

	private void init128() {
		cube_r762.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r763 = new FlowerPart(this);
		cube_r763.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal10.addChild(cube_r763);
		setRotationAngle(cube_r763, 0.0F, 0.0F, 1.8588F);
		cube_r763.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r764 = new FlowerPart(this);
		cube_r764.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal10.addChild(cube_r764);
		setRotationAngle(cube_r764, 0.0F, 0.0F, 1.0036F);
		cube_r764.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r765 = new FlowerPart(this);
		cube_r765.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal10.addChild(cube_r765);
		setRotationAngle(cube_r765, 0.0F, -0.0873F, 0.5236F);
		cube_r765.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r766 = new FlowerPart(this);
		cube_r766.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal10.addChild(cube_r766);
		setRotationAngle(cube_r766, 0.0F, 0.0873F, 0.5236F);
		cube_r766.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r767 = new FlowerPart(this);
		cube_r767.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal10.addChild(cube_r767);
		setRotationAngle(cube_r767, 0.0F, 0.0F, 0.5236F);
		cube_r767.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r767.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r768 = new FlowerPart(this);
		cube_r768.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal10.addChild(cube_r768);
		setRotationAngle(cube_r768, 0.0F, 0.0F, -0.1745F);
		cube_r768.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r769 = new FlowerPart(this);
	}

	private void init129() {
		cube_r769.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal10.addChild(cube_r769);
		setRotationAngle(cube_r769, 0.0F, -0.2618F, -0.1745F);
		cube_r769.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r770 = new FlowerPart(this);
		cube_r770.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal10.addChild(cube_r770);
		setRotationAngle(cube_r770, 0.0F, 0.2618F, -0.1745F);
		cube_r770.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r771 = new FlowerPart(this);
		cube_r771.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal10.addChild(cube_r771);
		setRotationAngle(cube_r771, 0.0F, 0.0F, -0.1745F);
		cube_r771.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal11 = new FlowerPart(this);
		tepal11.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth2.addChild(tepal11);
		setRotationAngle(tepal11, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r772 = new FlowerPart(this);
		cube_r772.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal11.addChild(cube_r772);
		setRotationAngle(cube_r772, 0.0F, 0.0F, 0.2574F);
		cube_r772.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r773 = new FlowerPart(this);
		cube_r773.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal11.addChild(cube_r773);
		setRotationAngle(cube_r773, -0.0167F, 0.1453F, 0.7743F);
		cube_r773.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r774 = new FlowerPart(this);
		cube_r774.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal11.addChild(cube_r774);
		setRotationAngle(cube_r774, -0.0167F, -0.1339F, 0.7789F);
		cube_r774.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init130() {

		cube_r775 = new FlowerPart(this);
		cube_r775.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal11.addChild(cube_r775);
		setRotationAngle(cube_r775, -0.0167F, -0.1339F, 0.7789F);
		cube_r775.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r776 = new FlowerPart(this);
		cube_r776.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal11.addChild(cube_r776);
		setRotationAngle(cube_r776, -0.0165F, 0.0057F, 0.7766F);
		cube_r776.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r776.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r776.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r777 = new FlowerPart(this);
		cube_r777.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal11.addChild(cube_r777);
		setRotationAngle(cube_r777, -0.0139F, -0.0105F, -0.2006F);
		cube_r777.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r778 = new FlowerPart(this);
		cube_r778.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal11.addChild(cube_r778);
		setRotationAngle(cube_r778, 0.0015F, -0.0174F, -1.213F);
		cube_r778.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r779 = new FlowerPart(this);
		cube_r779.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal11.addChild(cube_r779);
		setRotationAngle(cube_r779, 0.0F, 0.1047F, -2.4696F);
		cube_r779.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r780 = new FlowerPart(this);
		cube_r780.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal11.addChild(cube_r780);
		setRotationAngle(cube_r780, 0.0F, -0.1047F, -2.4696F);
		cube_r780.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r781 = new FlowerPart(this);
	}

	private void init131() {
		cube_r781.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal11.addChild(cube_r781);
		setRotationAngle(cube_r781, 0.0F, 0.0F, -2.4696F);
		cube_r781.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r781.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r781.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r782 = new FlowerPart(this);
		cube_r782.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal11.addChild(cube_r782);
		setRotationAngle(cube_r782, 0.0F, 0.0F, 2.8536F);
		cube_r782.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r783 = new FlowerPart(this);
		cube_r783.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal11.addChild(cube_r783);
		setRotationAngle(cube_r783, 0.0F, -0.0873F, 1.8588F);
		cube_r783.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r784 = new FlowerPart(this);
		cube_r784.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal11.addChild(cube_r784);
		setRotationAngle(cube_r784, 0.0F, 0.1309F, 1.8588F);
		cube_r784.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r785 = new FlowerPart(this);
		cube_r785.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal11.addChild(cube_r785);
		setRotationAngle(cube_r785, 0.0F, 0.0F, 1.8588F);
		cube_r785.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r785.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r786 = new FlowerPart(this);
		cube_r786.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal11.addChild(cube_r786);
		setRotationAngle(cube_r786, 0.0F, 0.0F, 1.8588F);
		cube_r786.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r787 = new FlowerPart(this);
		cube_r787.setRotationPoint(8.81F, 0.575F, 0.0F);
	}

	private void init132() {
		tepal11.addChild(cube_r787);
		setRotationAngle(cube_r787, 0.0F, 0.0F, 1.0036F);
		cube_r787.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r788 = new FlowerPart(this);
		cube_r788.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal11.addChild(cube_r788);
		setRotationAngle(cube_r788, 0.0F, -0.0873F, 0.5236F);
		cube_r788.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r789 = new FlowerPart(this);
		cube_r789.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal11.addChild(cube_r789);
		setRotationAngle(cube_r789, 0.0F, 0.0873F, 0.5236F);
		cube_r789.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r790 = new FlowerPart(this);
		cube_r790.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal11.addChild(cube_r790);
		setRotationAngle(cube_r790, 0.0F, 0.0F, 0.5236F);
		cube_r790.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r790.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r791 = new FlowerPart(this);
		cube_r791.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal11.addChild(cube_r791);
		setRotationAngle(cube_r791, 0.0F, 0.0F, -0.1745F);
		cube_r791.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r792 = new FlowerPart(this);
		cube_r792.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal11.addChild(cube_r792);
		setRotationAngle(cube_r792, 0.0F, -0.2618F, -0.1745F);
		cube_r792.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r793 = new FlowerPart(this);
		cube_r793.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal11.addChild(cube_r793);
		setRotationAngle(cube_r793, 0.0F, 0.2618F, -0.1745F);
		cube_r793.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init133() {

		cube_r794 = new FlowerPart(this);
		cube_r794.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal11.addChild(cube_r794);
		setRotationAngle(cube_r794, 0.0F, 0.0F, -0.1745F);
		cube_r794.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal12 = new FlowerPart(this);
		tepal12.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth2.addChild(tepal12);
		setRotationAngle(tepal12, 0.5348F, -0.9507F, -0.891F);
		

		cube_r795 = new FlowerPart(this);
		cube_r795.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal12.addChild(cube_r795);
		setRotationAngle(cube_r795, 0.0F, 0.0F, 0.2574F);
		cube_r795.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r796 = new FlowerPart(this);
		cube_r796.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal12.addChild(cube_r796);
		setRotationAngle(cube_r796, -0.0167F, 0.1453F, 0.7743F);
		cube_r796.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r797 = new FlowerPart(this);
		cube_r797.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal12.addChild(cube_r797);
		setRotationAngle(cube_r797, -0.0167F, -0.1339F, 0.7789F);
		cube_r797.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r798 = new FlowerPart(this);
		cube_r798.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal12.addChild(cube_r798);
		setRotationAngle(cube_r798, -0.0167F, -0.1339F, 0.7789F);
		cube_r798.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r799 = new FlowerPart(this);
		cube_r799.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal12.addChild(cube_r799);
	}

	private void init134() {
		setRotationAngle(cube_r799, -0.0165F, 0.0057F, 0.7766F);
		cube_r799.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r799.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r799.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r800 = new FlowerPart(this);
		cube_r800.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal12.addChild(cube_r800);
		setRotationAngle(cube_r800, -0.0139F, -0.0105F, -0.2006F);
		cube_r800.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r801 = new FlowerPart(this);
		cube_r801.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal12.addChild(cube_r801);
		setRotationAngle(cube_r801, 0.0015F, -0.0174F, -1.213F);
		cube_r801.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r802 = new FlowerPart(this);
		cube_r802.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal12.addChild(cube_r802);
		setRotationAngle(cube_r802, 0.0F, 0.1047F, -2.4696F);
		cube_r802.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r803 = new FlowerPart(this);
		cube_r803.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal12.addChild(cube_r803);
		setRotationAngle(cube_r803, 0.0F, -0.1047F, -2.4696F);
		cube_r803.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r804 = new FlowerPart(this);
		cube_r804.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal12.addChild(cube_r804);
		setRotationAngle(cube_r804, 0.0F, 0.0F, -2.4696F);
		cube_r804.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r804.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r804.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r805 = new FlowerPart(this);
		cube_r805.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal12.addChild(cube_r805);
	}

	private void init135() {
		setRotationAngle(cube_r805, 0.0F, 0.0F, 2.8536F);
		cube_r805.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r806 = new FlowerPart(this);
		cube_r806.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal12.addChild(cube_r806);
		setRotationAngle(cube_r806, 0.0F, -0.0873F, 1.8588F);
		cube_r806.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r807 = new FlowerPart(this);
		cube_r807.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal12.addChild(cube_r807);
		setRotationAngle(cube_r807, 0.0F, 0.1309F, 1.8588F);
		cube_r807.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r808 = new FlowerPart(this);
		cube_r808.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal12.addChild(cube_r808);
		setRotationAngle(cube_r808, 0.0F, 0.0F, 1.8588F);
		cube_r808.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r808.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r809 = new FlowerPart(this);
		cube_r809.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal12.addChild(cube_r809);
		setRotationAngle(cube_r809, 0.0F, 0.0F, 1.8588F);
		cube_r809.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r810 = new FlowerPart(this);
		cube_r810.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal12.addChild(cube_r810);
		setRotationAngle(cube_r810, 0.0F, 0.0F, 1.0036F);
		cube_r810.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r811 = new FlowerPart(this);
		cube_r811.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal12.addChild(cube_r811);
		setRotationAngle(cube_r811, 0.0F, -0.0873F, 0.5236F);
		cube_r811.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r812 = new FlowerPart(this);
	}

	private void init136() {
		cube_r812.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal12.addChild(cube_r812);
		setRotationAngle(cube_r812, 0.0F, 0.0873F, 0.5236F);
		cube_r812.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r813 = new FlowerPart(this);
		cube_r813.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal12.addChild(cube_r813);
		setRotationAngle(cube_r813, 0.0F, 0.0F, 0.5236F);
		cube_r813.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r813.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r814 = new FlowerPart(this);
		cube_r814.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal12.addChild(cube_r814);
		setRotationAngle(cube_r814, 0.0F, 0.0F, -0.1745F);
		cube_r814.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r815 = new FlowerPart(this);
		cube_r815.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal12.addChild(cube_r815);
		setRotationAngle(cube_r815, 0.0F, -0.2618F, -0.1745F);
		cube_r815.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r816 = new FlowerPart(this);
		cube_r816.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal12.addChild(cube_r816);
		setRotationAngle(cube_r816, 0.0F, 0.2618F, -0.1745F);
		cube_r816.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r817 = new FlowerPart(this);
		cube_r817.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal12.addChild(cube_r817);
		setRotationAngle(cube_r817, 0.0F, 0.0F, -0.1745F);
		cube_r817.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal7 = new FlowerPart(this);
		stemal7.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal7);
		setRotationAngle(stemal7, -0.4931F, -0.438F, -0.0571F);
	}

	private void init137() {
		stemal7.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r818 = new FlowerPart(this);
		cube_r818.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal7.addChild(cube_r818);
		setRotationAngle(cube_r818, 0.0F, 0.0F, 0.8814F);
		cube_r818.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r819 = new FlowerPart(this);
		cube_r819.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal7.addChild(cube_r819);
		setRotationAngle(cube_r819, 0.0F, 0.0F, 0.2618F);
		cube_r819.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r820 = new FlowerPart(this);
		cube_r820.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal7.addChild(cube_r820);
		setRotationAngle(cube_r820, 0.0F, 0.0F, 0.8814F);
		cube_r820.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r821 = new FlowerPart(this);
		cube_r821.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal7.addChild(cube_r821);
		setRotationAngle(cube_r821, 0.0F, 0.0F, 0.5498F);
		cube_r821.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r822 = new FlowerPart(this);
		cube_r822.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal7.addChild(cube_r822);
		setRotationAngle(cube_r822, 0.0F, 0.0F, 0.8814F);
		cube_r822.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal8 = new FlowerPart(this);
		stemal8.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal8);
		setRotationAngle(stemal8, -0.1017F, 0.0303F, 0.0497F);
		stemal8.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r823 = new FlowerPart(this);
		cube_r823.setRotationPoint(7.105F, -13.29F, 0.145F);
	}

	private void init138() {
		stemal8.addChild(cube_r823);
		setRotationAngle(cube_r823, 0.0F, 0.0F, 0.8814F);
		cube_r823.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r824 = new FlowerPart(this);
		cube_r824.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal8.addChild(cube_r824);
		setRotationAngle(cube_r824, 0.0F, 0.0F, 0.2618F);
		cube_r824.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r825 = new FlowerPart(this);
		cube_r825.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal8.addChild(cube_r825);
		setRotationAngle(cube_r825, 0.0F, 0.0F, 0.8814F);
		cube_r825.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r826 = new FlowerPart(this);
		cube_r826.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal8.addChild(cube_r826);
		setRotationAngle(cube_r826, 0.0F, 0.0F, 0.5498F);
		cube_r826.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r827 = new FlowerPart(this);
		cube_r827.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal8.addChild(cube_r827);
		setRotationAngle(cube_r827, 0.0F, 0.0F, 0.8814F);
		cube_r827.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal9 = new FlowerPart(this);
		stemal9.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal9);
		setRotationAngle(stemal9, 0.1582F, 0.2804F, -0.7053F);
		stemal9.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r828 = new FlowerPart(this);
		cube_r828.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal9.addChild(cube_r828);
		setRotationAngle(cube_r828, 0.0F, 0.0F, 0.8814F);
		cube_r828.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r829 = new FlowerPart(this);
	}

	private void init139() {
		cube_r829.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal9.addChild(cube_r829);
		setRotationAngle(cube_r829, 0.0F, 0.0F, 0.2618F);
		cube_r829.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r830 = new FlowerPart(this);
		cube_r830.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal9.addChild(cube_r830);
		setRotationAngle(cube_r830, 0.0F, 0.0F, 0.8814F);
		cube_r830.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r831 = new FlowerPart(this);
		cube_r831.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal9.addChild(cube_r831);
		setRotationAngle(cube_r831, 0.0F, 0.0F, 0.5498F);
		cube_r831.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r832 = new FlowerPart(this);
		cube_r832.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal9.addChild(cube_r832);
		setRotationAngle(cube_r832, 0.0F, 0.0F, 0.8814F);
		cube_r832.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal10 = new FlowerPart(this);
		stemal10.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal10);
		setRotationAngle(stemal10, -0.2949F, -0.1812F, -0.7237F);
		stemal10.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r833 = new FlowerPart(this);
		cube_r833.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal10.addChild(cube_r833);
		setRotationAngle(cube_r833, 0.0F, 0.0F, 0.8814F);
		cube_r833.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r834 = new FlowerPart(this);
		cube_r834.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal10.addChild(cube_r834);
		setRotationAngle(cube_r834, 0.0F, 0.0F, 0.2618F);
		cube_r834.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));
	}

	private void init140() {

		cube_r835 = new FlowerPart(this);
		cube_r835.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal10.addChild(cube_r835);
		setRotationAngle(cube_r835, 0.0F, 0.0F, 0.8814F);
		cube_r835.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r836 = new FlowerPart(this);
		cube_r836.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal10.addChild(cube_r836);
		setRotationAngle(cube_r836, 0.0F, 0.0F, 0.5498F);
		cube_r836.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r837 = new FlowerPart(this);
		cube_r837.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal10.addChild(cube_r837);
		setRotationAngle(cube_r837, 0.0F, 0.0F, 0.8814F);
		cube_r837.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal11 = new FlowerPart(this);
		stemal11.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal11);
		setRotationAngle(stemal11, 0.3149F, 0.1775F, -0.2915F);
		stemal11.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r838 = new FlowerPart(this);
		cube_r838.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal11.addChild(cube_r838);
		setRotationAngle(cube_r838, 0.0F, 0.0F, 0.8814F);
		cube_r838.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r839 = new FlowerPart(this);
		cube_r839.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal11.addChild(cube_r839);
		setRotationAngle(cube_r839, 0.0F, 0.0F, 0.2618F);
		cube_r839.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r840 = new FlowerPart(this);
		cube_r840.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal11.addChild(cube_r840);
	}

	private void init141() {
		setRotationAngle(cube_r840, 0.0F, 0.0F, 0.8814F);
		cube_r840.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r841 = new FlowerPart(this);
		cube_r841.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal11.addChild(cube_r841);
		setRotationAngle(cube_r841, 0.0F, 0.0F, 0.5498F);
		cube_r841.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r842 = new FlowerPart(this);
		cube_r842.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal11.addChild(cube_r842);
		setRotationAngle(cube_r842, 0.0F, 0.0F, 0.8814F);
		cube_r842.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal12 = new FlowerPart(this);
		stemal12.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth2.addChild(stemal12);
		setRotationAngle(stemal12, 0.0F, 0.0F, -0.9163F);
		stemal12.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r843 = new FlowerPart(this);
		cube_r843.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal12.addChild(cube_r843);
		setRotationAngle(cube_r843, 0.0F, 0.0F, 0.8814F);
		cube_r843.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r844 = new FlowerPart(this);
		cube_r844.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal12.addChild(cube_r844);
		setRotationAngle(cube_r844, 0.0F, 0.0F, 0.2618F);
		cube_r844.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r845 = new FlowerPart(this);
		cube_r845.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal12.addChild(cube_r845);
		setRotationAngle(cube_r845, 0.0F, 0.0F, 0.8814F);
		cube_r845.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r846 = new FlowerPart(this);
	}

	private void init142() {
		cube_r846.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal12.addChild(cube_r846);
		setRotationAngle(cube_r846, 0.0F, 0.0F, 0.5498F);
		cube_r846.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r847 = new FlowerPart(this);
		cube_r847.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal12.addChild(cube_r847);
		setRotationAngle(cube_r847, 0.0F, 0.0F, 0.8814F);
		cube_r847.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		perianth = new FlowerPart(this);
		perianth.setRotationPoint(-18.865F, -0.31F, -1.805F);
		half2.addChild(perianth);
		setRotationAngle(perianth, -0.4291F, 0.5638F, -0.7679F);
		

		tepal1 = new FlowerPart(this);
		tepal1.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal1);
		setRotationAngle(tepal1, 0.0F, 0.0F, -0.6109F);
		

		cube_r848 = new FlowerPart(this);
		cube_r848.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal1.addChild(cube_r848);
		setRotationAngle(cube_r848, 0.0F, 0.0F, 0.2574F);
		cube_r848.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r849 = new FlowerPart(this);
		cube_r849.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal1.addChild(cube_r849);
		setRotationAngle(cube_r849, -0.0167F, 0.1453F, 0.7743F);
		cube_r849.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r850 = new FlowerPart(this);
		cube_r850.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal1.addChild(cube_r850);
		setRotationAngle(cube_r850, -0.0167F, -0.1339F, 0.7789F);
		cube_r850.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));
	}

	private void init143() {

		cube_r851 = new FlowerPart(this);
		cube_r851.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal1.addChild(cube_r851);
		setRotationAngle(cube_r851, -0.0167F, -0.1339F, 0.7789F);
		cube_r851.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r852 = new FlowerPart(this);
		cube_r852.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal1.addChild(cube_r852);
		setRotationAngle(cube_r852, -0.0165F, 0.0057F, 0.7766F);
		cube_r852.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r852.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r852.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r853 = new FlowerPart(this);
		cube_r853.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal1.addChild(cube_r853);
		setRotationAngle(cube_r853, -0.0139F, -0.0105F, -0.2006F);
		cube_r853.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r854 = new FlowerPart(this);
		cube_r854.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal1.addChild(cube_r854);
		setRotationAngle(cube_r854, 0.0015F, -0.0174F, -1.213F);
		cube_r854.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r855 = new FlowerPart(this);
		cube_r855.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal1.addChild(cube_r855);
		setRotationAngle(cube_r855, 0.0F, 0.1047F, -2.4696F);
		cube_r855.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r856 = new FlowerPart(this);
		cube_r856.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal1.addChild(cube_r856);
		setRotationAngle(cube_r856, 0.0F, -0.1047F, -2.4696F);
		cube_r856.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r857 = new FlowerPart(this);
	}

	private void init144() {
		cube_r857.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal1.addChild(cube_r857);
		setRotationAngle(cube_r857, 0.0F, 0.0F, -2.4696F);
		cube_r857.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r857.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r857.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r858 = new FlowerPart(this);
		cube_r858.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal1.addChild(cube_r858);
		setRotationAngle(cube_r858, 0.0F, 0.0F, 2.8536F);
		cube_r858.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r859 = new FlowerPart(this);
		cube_r859.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal1.addChild(cube_r859);
		setRotationAngle(cube_r859, 0.0F, -0.0873F, 1.8588F);
		cube_r859.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r860 = new FlowerPart(this);
		cube_r860.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal1.addChild(cube_r860);
		setRotationAngle(cube_r860, 0.0F, 0.1309F, 1.8588F);
		cube_r860.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r861 = new FlowerPart(this);
		cube_r861.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal1.addChild(cube_r861);
		setRotationAngle(cube_r861, 0.0F, 0.0F, 1.8588F);
		cube_r861.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r861.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r862 = new FlowerPart(this);
		cube_r862.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal1.addChild(cube_r862);
		setRotationAngle(cube_r862, 0.0F, 0.0F, 1.8588F);
		cube_r862.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r863 = new FlowerPart(this);
		cube_r863.setRotationPoint(8.81F, 0.575F, 0.0F);
	}

	private void init145() {
		tepal1.addChild(cube_r863);
		setRotationAngle(cube_r863, 0.0F, 0.0F, 1.0036F);
		cube_r863.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r864 = new FlowerPart(this);
		cube_r864.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal1.addChild(cube_r864);
		setRotationAngle(cube_r864, 0.0F, -0.0873F, 0.5236F);
		cube_r864.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r865 = new FlowerPart(this);
		cube_r865.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal1.addChild(cube_r865);
		setRotationAngle(cube_r865, 0.0F, 0.0873F, 0.5236F);
		cube_r865.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r866 = new FlowerPart(this);
		cube_r866.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal1.addChild(cube_r866);
		setRotationAngle(cube_r866, 0.0F, 0.0F, 0.5236F);
		cube_r866.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r866.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r867 = new FlowerPart(this);
		cube_r867.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal1.addChild(cube_r867);
		setRotationAngle(cube_r867, 0.0F, 0.0F, -0.1745F);
		cube_r867.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r868 = new FlowerPart(this);
		cube_r868.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal1.addChild(cube_r868);
		setRotationAngle(cube_r868, 0.0F, -0.2618F, -0.1745F);
		cube_r868.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r869 = new FlowerPart(this);
		cube_r869.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal1.addChild(cube_r869);
		setRotationAngle(cube_r869, 0.0F, 0.2618F, -0.1745F);
		cube_r869.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));
	}

	private void init146() {

		cube_r870 = new FlowerPart(this);
		cube_r870.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal1.addChild(cube_r870);
		setRotationAngle(cube_r870, 0.0F, 0.0F, -0.1745F);
		cube_r870.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal2 = new FlowerPart(this);
		tepal2.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal2);
		setRotationAngle(tepal2, -0.8861F, 0.6591F, -1.3689F);
		

		cube_r871 = new FlowerPart(this);
		cube_r871.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal2.addChild(cube_r871);
		setRotationAngle(cube_r871, 0.0F, 0.0F, 0.2574F);
		cube_r871.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r872 = new FlowerPart(this);
		cube_r872.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal2.addChild(cube_r872);
		setRotationAngle(cube_r872, -0.0167F, 0.1453F, 0.7743F);
		cube_r872.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r873 = new FlowerPart(this);
		cube_r873.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal2.addChild(cube_r873);
		setRotationAngle(cube_r873, -0.0167F, -0.1339F, 0.7789F);
		cube_r873.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r874 = new FlowerPart(this);
		cube_r874.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal2.addChild(cube_r874);
		setRotationAngle(cube_r874, -0.0167F, -0.1339F, 0.7789F);
		cube_r874.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r875 = new FlowerPart(this);
		cube_r875.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal2.addChild(cube_r875);
	}

	private void init147() {
		setRotationAngle(cube_r875, -0.0165F, 0.0057F, 0.7766F);
		cube_r875.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r875.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r875.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r876 = new FlowerPart(this);
		cube_r876.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal2.addChild(cube_r876);
		setRotationAngle(cube_r876, -0.0139F, -0.0105F, -0.2006F);
		cube_r876.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r877 = new FlowerPart(this);
		cube_r877.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal2.addChild(cube_r877);
		setRotationAngle(cube_r877, 0.0015F, -0.0174F, -1.213F);
		cube_r877.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r878 = new FlowerPart(this);
		cube_r878.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal2.addChild(cube_r878);
		setRotationAngle(cube_r878, 0.0F, 0.1047F, -2.4696F);
		cube_r878.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r879 = new FlowerPart(this);
		cube_r879.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal2.addChild(cube_r879);
		setRotationAngle(cube_r879, 0.0F, -0.1047F, -2.4696F);
		cube_r879.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r880 = new FlowerPart(this);
		cube_r880.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal2.addChild(cube_r880);
		setRotationAngle(cube_r880, 0.0F, 0.0F, -2.4696F);
		cube_r880.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r880.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r880.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r881 = new FlowerPart(this);
		cube_r881.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal2.addChild(cube_r881);
	}

	private void init148() {
		setRotationAngle(cube_r881, 0.0F, 0.0F, 2.8536F);
		cube_r881.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r882 = new FlowerPart(this);
		cube_r882.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal2.addChild(cube_r882);
		setRotationAngle(cube_r882, 0.0F, -0.0873F, 1.8588F);
		cube_r882.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r883 = new FlowerPart(this);
		cube_r883.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal2.addChild(cube_r883);
		setRotationAngle(cube_r883, 0.0F, 0.1309F, 1.8588F);
		cube_r883.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r884 = new FlowerPart(this);
		cube_r884.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal2.addChild(cube_r884);
		setRotationAngle(cube_r884, 0.0F, 0.0F, 1.8588F);
		cube_r884.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r884.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r885 = new FlowerPart(this);
		cube_r885.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal2.addChild(cube_r885);
		setRotationAngle(cube_r885, 0.0F, 0.0F, 1.8588F);
		cube_r885.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r886 = new FlowerPart(this);
		cube_r886.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal2.addChild(cube_r886);
		setRotationAngle(cube_r886, 0.0F, 0.0F, 1.0036F);
		cube_r886.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r887 = new FlowerPart(this);
		cube_r887.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal2.addChild(cube_r887);
		setRotationAngle(cube_r887, 0.0F, -0.0873F, 0.5236F);
		cube_r887.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r888 = new FlowerPart(this);
	}

	private void init149() {
		cube_r888.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal2.addChild(cube_r888);
		setRotationAngle(cube_r888, 0.0F, 0.0873F, 0.5236F);
		cube_r888.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r889 = new FlowerPart(this);
		cube_r889.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal2.addChild(cube_r889);
		setRotationAngle(cube_r889, 0.0F, 0.0F, 0.5236F);
		cube_r889.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r889.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r890 = new FlowerPart(this);
		cube_r890.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal2.addChild(cube_r890);
		setRotationAngle(cube_r890, 0.0F, 0.0F, -0.1745F);
		cube_r890.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r891 = new FlowerPart(this);
		cube_r891.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal2.addChild(cube_r891);
		setRotationAngle(cube_r891, 0.0F, -0.2618F, -0.1745F);
		cube_r891.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r892 = new FlowerPart(this);
		cube_r892.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal2.addChild(cube_r892);
		setRotationAngle(cube_r892, 0.0F, 0.2618F, -0.1745F);
		cube_r892.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r893 = new FlowerPart(this);
		cube_r893.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal2.addChild(cube_r893);
		setRotationAngle(cube_r893, 0.0F, 0.0F, -0.1745F);
		cube_r893.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal3 = new FlowerPart(this);
		tepal3.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal3);
		setRotationAngle(tepal3, -2.6068F, 0.9507F, -2.7742F);
	}

	private void init150() {
		

		cube_r894 = new FlowerPart(this);
		cube_r894.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal3.addChild(cube_r894);
		setRotationAngle(cube_r894, 0.0F, 0.0F, 0.2574F);
		cube_r894.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r895 = new FlowerPart(this);
		cube_r895.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal3.addChild(cube_r895);
		setRotationAngle(cube_r895, -0.0167F, 0.1453F, 0.7743F);
		cube_r895.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r896 = new FlowerPart(this);
		cube_r896.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal3.addChild(cube_r896);
		setRotationAngle(cube_r896, -0.0167F, -0.1339F, 0.7789F);
		cube_r896.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r897 = new FlowerPart(this);
		cube_r897.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal3.addChild(cube_r897);
		setRotationAngle(cube_r897, -0.0167F, -0.1339F, 0.7789F);
		cube_r897.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r898 = new FlowerPart(this);
		cube_r898.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal3.addChild(cube_r898);
		setRotationAngle(cube_r898, -0.0165F, 0.0057F, 0.7766F);
		cube_r898.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r898.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r898.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r899 = new FlowerPart(this);
		cube_r899.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal3.addChild(cube_r899);
		setRotationAngle(cube_r899, -0.0139F, -0.0105F, -0.2006F);
		cube_r899.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r900 = new FlowerPart(this);
	}

	private void init151() {
		cube_r900.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal3.addChild(cube_r900);
		setRotationAngle(cube_r900, 0.0015F, -0.0174F, -1.213F);
		cube_r900.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r901 = new FlowerPart(this);
		cube_r901.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal3.addChild(cube_r901);
		setRotationAngle(cube_r901, 0.0F, 0.1047F, -2.4696F);
		cube_r901.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r902 = new FlowerPart(this);
		cube_r902.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal3.addChild(cube_r902);
		setRotationAngle(cube_r902, 0.0F, -0.1047F, -2.4696F);
		cube_r902.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r903 = new FlowerPart(this);
		cube_r903.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal3.addChild(cube_r903);
		setRotationAngle(cube_r903, 0.0F, 0.0F, -2.4696F);
		cube_r903.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r903.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r903.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r904 = new FlowerPart(this);
		cube_r904.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal3.addChild(cube_r904);
		setRotationAngle(cube_r904, 0.0F, 0.0F, 2.8536F);
		cube_r904.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r905 = new FlowerPart(this);
		cube_r905.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal3.addChild(cube_r905);
		setRotationAngle(cube_r905, 0.0F, -0.0873F, 1.8588F);
		cube_r905.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r906 = new FlowerPart(this);
		cube_r906.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal3.addChild(cube_r906);
	}

	private void init152() {
		setRotationAngle(cube_r906, 0.0F, 0.1309F, 1.8588F);
		cube_r906.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r907 = new FlowerPart(this);
		cube_r907.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal3.addChild(cube_r907);
		setRotationAngle(cube_r907, 0.0F, 0.0F, 1.8588F);
		cube_r907.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r907.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r908 = new FlowerPart(this);
		cube_r908.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal3.addChild(cube_r908);
		setRotationAngle(cube_r908, 0.0F, 0.0F, 1.8588F);
		cube_r908.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r909 = new FlowerPart(this);
		cube_r909.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal3.addChild(cube_r909);
		setRotationAngle(cube_r909, 0.0F, 0.0F, 1.0036F);
		cube_r909.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r910 = new FlowerPart(this);
		cube_r910.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal3.addChild(cube_r910);
		setRotationAngle(cube_r910, 0.0F, -0.0873F, 0.5236F);
		cube_r910.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r911 = new FlowerPart(this);
		cube_r911.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal3.addChild(cube_r911);
		setRotationAngle(cube_r911, 0.0F, 0.0873F, 0.5236F);
		cube_r911.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r912 = new FlowerPart(this);
		cube_r912.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal3.addChild(cube_r912);
		setRotationAngle(cube_r912, 0.0F, 0.0F, 0.5236F);
		cube_r912.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r912.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));
	}

	private void init153() {

		cube_r913 = new FlowerPart(this);
		cube_r913.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal3.addChild(cube_r913);
		setRotationAngle(cube_r913, 0.0F, 0.0F, -0.1745F);
		cube_r913.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r914 = new FlowerPart(this);
		cube_r914.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal3.addChild(cube_r914);
		setRotationAngle(cube_r914, 0.0F, -0.2618F, -0.1745F);
		cube_r914.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r915 = new FlowerPart(this);
		cube_r915.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal3.addChild(cube_r915);
		setRotationAngle(cube_r915, 0.0F, 0.2618F, -0.1745F);
		cube_r915.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r916 = new FlowerPart(this);
		cube_r916.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal3.addChild(cube_r916);
		setRotationAngle(cube_r916, 0.0F, 0.0F, -0.1745F);
		cube_r916.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal4 = new FlowerPart(this);
		tepal4.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal4);
		setRotationAngle(tepal4, -3.1416F, 0.0F, -3.0543F);
		

		cube_r917 = new FlowerPart(this);
		cube_r917.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal4.addChild(cube_r917);
		setRotationAngle(cube_r917, 0.0F, 0.0F, 0.2574F);
		cube_r917.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r918 = new FlowerPart(this);
		cube_r918.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal4.addChild(cube_r918);
	}

	private void init154() {
		setRotationAngle(cube_r918, -0.0167F, 0.1453F, 0.7743F);
		cube_r918.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r919 = new FlowerPart(this);
		cube_r919.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal4.addChild(cube_r919);
		setRotationAngle(cube_r919, -0.0167F, -0.1339F, 0.7789F);
		cube_r919.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r920 = new FlowerPart(this);
		cube_r920.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal4.addChild(cube_r920);
		setRotationAngle(cube_r920, -0.0167F, -0.1339F, 0.7789F);
		cube_r920.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r921 = new FlowerPart(this);
		cube_r921.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal4.addChild(cube_r921);
		setRotationAngle(cube_r921, -0.0165F, 0.0057F, 0.7766F);
		cube_r921.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r921.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r921.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r922 = new FlowerPart(this);
		cube_r922.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal4.addChild(cube_r922);
		setRotationAngle(cube_r922, -0.0139F, -0.0105F, -0.2006F);
		cube_r922.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r923 = new FlowerPart(this);
		cube_r923.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal4.addChild(cube_r923);
		setRotationAngle(cube_r923, 0.0015F, -0.0174F, -1.213F);
		cube_r923.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r924 = new FlowerPart(this);
		cube_r924.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal4.addChild(cube_r924);
		setRotationAngle(cube_r924, 0.0F, 0.1047F, -2.4696F);
		cube_r924.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));
	}

	private void init155() {

		cube_r925 = new FlowerPart(this);
		cube_r925.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal4.addChild(cube_r925);
		setRotationAngle(cube_r925, 0.0F, -0.1047F, -2.4696F);
		cube_r925.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r926 = new FlowerPart(this);
		cube_r926.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal4.addChild(cube_r926);
		setRotationAngle(cube_r926, 0.0F, 0.0F, -2.4696F);
		cube_r926.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r926.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r926.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r927 = new FlowerPart(this);
		cube_r927.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal4.addChild(cube_r927);
		setRotationAngle(cube_r927, 0.0F, 0.0F, 2.8536F);
		cube_r927.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r928 = new FlowerPart(this);
		cube_r928.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal4.addChild(cube_r928);
		setRotationAngle(cube_r928, 0.0F, -0.0873F, 1.8588F);
		cube_r928.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r929 = new FlowerPart(this);
		cube_r929.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal4.addChild(cube_r929);
		setRotationAngle(cube_r929, 0.0F, 0.1309F, 1.8588F);
		cube_r929.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r930 = new FlowerPart(this);
		cube_r930.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal4.addChild(cube_r930);
		setRotationAngle(cube_r930, 0.0F, 0.0F, 1.8588F);
		cube_r930.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r930.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r931 = new FlowerPart(this);
	}

	private void init156() {
		cube_r931.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal4.addChild(cube_r931);
		setRotationAngle(cube_r931, 0.0F, 0.0F, 1.8588F);
		cube_r931.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r932 = new FlowerPart(this);
		cube_r932.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal4.addChild(cube_r932);
		setRotationAngle(cube_r932, 0.0F, 0.0F, 1.0036F);
		cube_r932.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r933 = new FlowerPart(this);
		cube_r933.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal4.addChild(cube_r933);
		setRotationAngle(cube_r933, 0.0F, -0.0873F, 0.5236F);
		cube_r933.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r934 = new FlowerPart(this);
		cube_r934.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal4.addChild(cube_r934);
		setRotationAngle(cube_r934, 0.0F, 0.0873F, 0.5236F);
		cube_r934.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r935 = new FlowerPart(this);
		cube_r935.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal4.addChild(cube_r935);
		setRotationAngle(cube_r935, 0.0F, 0.0F, 0.5236F);
		cube_r935.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r935.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r936 = new FlowerPart(this);
		cube_r936.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal4.addChild(cube_r936);
		setRotationAngle(cube_r936, 0.0F, 0.0F, -0.1745F);
		cube_r936.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r937 = new FlowerPart(this);
		cube_r937.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal4.addChild(cube_r937);
		setRotationAngle(cube_r937, 0.0F, -0.2618F, -0.1745F);
	}

	private void init157() {
		cube_r937.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r938 = new FlowerPart(this);
		cube_r938.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal4.addChild(cube_r938);
		setRotationAngle(cube_r938, 0.0F, 0.2618F, -0.1745F);
		cube_r938.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r939 = new FlowerPart(this);
		cube_r939.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal4.addChild(cube_r939);
		setRotationAngle(cube_r939, 0.0F, 0.0F, -0.1745F);
		cube_r939.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal5 = new FlowerPart(this);
		tepal5.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal5);
		setRotationAngle(tepal5, 2.6068F, -0.9507F, -2.7742F);
		

		cube_r940 = new FlowerPart(this);
		cube_r940.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal5.addChild(cube_r940);
		setRotationAngle(cube_r940, 0.0F, 0.0F, 0.2574F);
		cube_r940.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r941 = new FlowerPart(this);
		cube_r941.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal5.addChild(cube_r941);
		setRotationAngle(cube_r941, -0.0167F, 0.1453F, 0.7743F);
		cube_r941.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r942 = new FlowerPart(this);
		cube_r942.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal5.addChild(cube_r942);
		setRotationAngle(cube_r942, -0.0167F, -0.1339F, 0.7789F);
		cube_r942.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r943 = new FlowerPart(this);
		cube_r943.setRotationPoint(8.955F, 3.28F, -0.245F);
	}

	private void init158() {
		tepal5.addChild(cube_r943);
		setRotationAngle(cube_r943, -0.0167F, -0.1339F, 0.7789F);
		cube_r943.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r944 = new FlowerPart(this);
		cube_r944.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal5.addChild(cube_r944);
		setRotationAngle(cube_r944, -0.0165F, 0.0057F, 0.7766F);
		cube_r944.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r944.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
		cube_r944.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r945 = new FlowerPart(this);
		cube_r945.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal5.addChild(cube_r945);
		setRotationAngle(cube_r945, -0.0139F, -0.0105F, -0.2006F);
		cube_r945.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r946 = new FlowerPart(this);
		cube_r946.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal5.addChild(cube_r946);
		setRotationAngle(cube_r946, 0.0015F, -0.0174F, -1.213F);
		cube_r946.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r947 = new FlowerPart(this);
		cube_r947.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal5.addChild(cube_r947);
		setRotationAngle(cube_r947, 0.0F, 0.1047F, -2.4696F);
		cube_r947.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r948 = new FlowerPart(this);
		cube_r948.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal5.addChild(cube_r948);
		setRotationAngle(cube_r948, 0.0F, -0.1047F, -2.4696F);
		cube_r948.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r949 = new FlowerPart(this);
		cube_r949.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal5.addChild(cube_r949);
		setRotationAngle(cube_r949, 0.0F, 0.0F, -2.4696F);
	}

	private void init159() {
		cube_r949.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r949.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r949.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r950 = new FlowerPart(this);
		cube_r950.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal5.addChild(cube_r950);
		setRotationAngle(cube_r950, 0.0F, 0.0F, 2.8536F);
		cube_r950.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r951 = new FlowerPart(this);
		cube_r951.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal5.addChild(cube_r951);
		setRotationAngle(cube_r951, 0.0F, -0.0873F, 1.8588F);
		cube_r951.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r952 = new FlowerPart(this);
		cube_r952.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal5.addChild(cube_r952);
		setRotationAngle(cube_r952, 0.0F, 0.1309F, 1.8588F);
		cube_r952.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r953 = new FlowerPart(this);
		cube_r953.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal5.addChild(cube_r953);
		setRotationAngle(cube_r953, 0.0F, 0.0F, 1.8588F);
		cube_r953.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r953.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r954 = new FlowerPart(this);
		cube_r954.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal5.addChild(cube_r954);
		setRotationAngle(cube_r954, 0.0F, 0.0F, 1.8588F);
		cube_r954.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r955 = new FlowerPart(this);
		cube_r955.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal5.addChild(cube_r955);
		setRotationAngle(cube_r955, 0.0F, 0.0F, 1.0036F);
		cube_r955.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));
	}

	private void init160() {

		cube_r956 = new FlowerPart(this);
		cube_r956.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal5.addChild(cube_r956);
		setRotationAngle(cube_r956, 0.0F, -0.0873F, 0.5236F);
		cube_r956.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r957 = new FlowerPart(this);
		cube_r957.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal5.addChild(cube_r957);
		setRotationAngle(cube_r957, 0.0F, 0.0873F, 0.5236F);
		cube_r957.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r958 = new FlowerPart(this);
		cube_r958.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal5.addChild(cube_r958);
		setRotationAngle(cube_r958, 0.0F, 0.0F, 0.5236F);
		cube_r958.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r958.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r959 = new FlowerPart(this);
		cube_r959.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal5.addChild(cube_r959);
		setRotationAngle(cube_r959, 0.0F, 0.0F, -0.1745F);
		cube_r959.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r960 = new FlowerPart(this);
		cube_r960.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal5.addChild(cube_r960);
		setRotationAngle(cube_r960, 0.0F, -0.2618F, -0.1745F);
		cube_r960.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r961 = new FlowerPart(this);
		cube_r961.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal5.addChild(cube_r961);
		setRotationAngle(cube_r961, 0.0F, 0.2618F, -0.1745F);
		cube_r961.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r962 = new FlowerPart(this);
		cube_r962.setRotationPoint(1.27F, -1.05F, -0.6F);
	}

	private void init161() {
		tepal5.addChild(cube_r962);
		setRotationAngle(cube_r962, 0.0F, 0.0F, -0.1745F);
		cube_r962.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		tepal6 = new FlowerPart(this);
		tepal6.setRotationPoint(0.0F, 0.0F, 0.0F);
		perianth.addChild(tepal6);
		setRotationAngle(tepal6, 0.5348F, -0.9507F, -0.891F);
		

		cube_r963 = new FlowerPart(this);
		cube_r963.setRotationPoint(7.0F, 0.0F, 0.0F);
		tepal6.addChild(cube_r963);
		setRotationAngle(cube_r963, 0.0F, 0.0F, 0.2574F);
		cube_r963.floatCubes.add(new FloatCube(0, 0, -4.08F, -0.5786F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false));

		cube_r964 = new FlowerPart(this);
		cube_r964.setRotationPoint(9.015F, 3.26F, 0.05F);
		tepal6.addChild(cube_r964);
		setRotationAngle(cube_r964, -0.0167F, 0.1453F, 0.7743F);
		cube_r964.floatCubes.add(new FloatCube(0, 0, -0.0644F, 0.0637F, 0.0F, 1.065F, 0.0F, 0.155F, 0.0F, false));

		cube_r965 = new FlowerPart(this);
		cube_r965.setRotationPoint(8.945F, 3.27F, -0.245F);
		tepal6.addChild(cube_r965);
		setRotationAngle(cube_r965, -0.0167F, -0.1339F, 0.7789F);
		cube_r965.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.07F, 0.0F, 0.155F, 0.0F, false));

		cube_r966 = new FlowerPart(this);
		cube_r966.setRotationPoint(8.955F, 3.28F, -0.245F);
		tepal6.addChild(cube_r966);
		setRotationAngle(cube_r966, -0.0167F, -0.1339F, 0.7789F);
		cube_r966.floatCubes.add(new FloatCube(0, 0, -0.0044F, 0.0037F, 0.0F, 1.0F, 0.0F, 0.155F, 0.0F, false));

		cube_r967 = new FlowerPart(this);
		cube_r967.setRotationPoint(8.89F, 3.21F, -0.15F);
		tepal6.addChild(cube_r967);
		setRotationAngle(cube_r967, -0.0165F, 0.0057F, 0.7766F);
		cube_r967.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.095F, 0.115F, 0.0F, 0.46F, 0.0F, false));
		cube_r967.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.0F, 0.0F, 0.16F, 0.0F, false));
	}

	private void init162() {
		cube_r967.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, 0.055F, 1.15F, 0.0F, 0.16F, 0.0F, false));

		cube_r968 = new FlowerPart(this);
		cube_r968.setRotationPoint(7.905F, 3.405F, 0.0F);
		tepal6.addChild(cube_r968);
		setRotationAngle(cube_r968, -0.0139F, -0.0105F, -0.2006F);
		cube_r968.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.255F, 1.0F, 0.0F, 0.46F, 0.0F, false));

		cube_r969 = new FlowerPart(this);
		cube_r969.setRotationPoint(7.55F, 4.345F, 0.0F);
		tepal6.addChild(cube_r969);
		setRotationAngle(cube_r969, 0.0015F, -0.0174F, -1.213F);
		cube_r969.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 1.0F, 0.0F, 0.465F, 0.0F, false));

		cube_r970 = new FlowerPart(this);
		cube_r970.setRotationPoint(9.11F, 5.45F, 0.28F);
		tepal6.addChild(cube_r970);
		setRotationAngle(cube_r970, 0.0F, 0.1047F, -2.4696F);
		cube_r970.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r971 = new FlowerPart(this);
		cube_r971.setRotationPoint(9.11F, 5.45F, -0.48F);
		tepal6.addChild(cube_r971);
		setRotationAngle(cube_r971, 0.0F, -0.1047F, -2.4696F);
		cube_r971.floatCubes.add(new FloatCube(0, 0, 0.0368F, -0.1057F, 0.0082F, 1.87F, 0.0F, 0.1F, 0.0F, false));

		cube_r972 = new FlowerPart(this);
		cube_r972.setRotationPoint(9.11F, 5.595F, 0.0F);
		tepal6.addChild(cube_r972);
		setRotationAngle(cube_r972, 0.0F, 0.0F, -2.4696F);
		cube_r972.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.37F, 1.025F, 0.0F, 0.655F, 0.0F, false));
		cube_r972.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.275F, 2.0F, 0.0F, 0.465F, 0.0F, false));
		cube_r972.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.47F, 0.15F, 0.0F, 0.855F, 0.0F, false));

		cube_r973 = new FlowerPart(this);
		cube_r973.setRotationPoint(11.035F, 5.03F, 0.0F);
		tepal6.addChild(cube_r973);
		setRotationAngle(cube_r973, 0.0F, 0.0F, 2.8536F);
		cube_r973.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.865F, 0.0F, false));

		cube_r974 = new FlowerPart(this);
	}

	private void init163() {
		cube_r974.setRotationPoint(11.57F, 3.12F, -0.65F);
		tepal6.addChild(cube_r974);
		setRotationAngle(cube_r974, 0.0F, -0.0873F, 1.8588F);
		cube_r974.floatCubes.add(new FloatCube(0, 0, -0.003F, -0.0273F, 0.0F, 2.0F, 0.0F, 0.235F, 0.0F, false));

		cube_r975 = new FlowerPart(this);
		cube_r975.setRotationPoint(11.6F, 3.135F, 0.65F);
		tepal6.addChild(cube_r975);
		setRotationAngle(cube_r975, 0.0F, 0.1309F, 1.8588F);
		cube_r975.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.26F, 2.0F, 0.0F, 0.26F, 0.0F, false));

		cube_r976 = new FlowerPart(this);
		cube_r976.setRotationPoint(11.61F, 3.11F, 0.06F);
		tepal6.addChild(cube_r976);
		setRotationAngle(cube_r976, 0.0F, 0.0F, 1.8588F);
		cube_r976.floatCubes.add(new FloatCube(0, 0, -0.003F, 0.0077F, -0.475F, 2.0F, 0.0F, 0.805F, 0.0F, false));
		cube_r976.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.68F, 0.165F, 0.0F, 1.25F, 0.0F, false));

		cube_r977 = new FlowerPart(this);
		cube_r977.setRotationPoint(11.61F, 3.1F, 0.06F);
		tepal6.addChild(cube_r977);
		setRotationAngle(cube_r977, 0.0F, 0.0F, 1.8588F);
		cube_r977.floatCubes.add(new FloatCube(0, 5, -0.003F, 0.0077F, -0.71F, 0.05F, 0.0F, 1.3F, 0.0F, false));

		cube_r978 = new FlowerPart(this);
		cube_r978.setRotationPoint(8.81F, 0.575F, 0.0F);
		tepal6.addChild(cube_r978);
		setRotationAngle(cube_r978, 0.0F, 0.0F, 1.0036F);
		cube_r978.floatCubes.add(new FloatCube(0, 4, 1.635F, -1.0F, -0.65F, 2.0F, 0.0F, 1.3F, 0.0F, false));

		cube_r979 = new FlowerPart(this);
		cube_r979.setRotationPoint(7.075F, -0.575F, -1.0F);
		tepal6.addChild(cube_r979);
		setRotationAngle(cube_r979, 0.0F, -0.0873F, 0.5236F);
		cube_r979.floatCubes.add(new FloatCube(0, 0, -0.0085F, -0.0015F, 0.0F, 4.045F, 0.0F, 0.6F, 0.0F, false));

		cube_r980 = new FlowerPart(this);
		cube_r980.setRotationPoint(7.075F, -0.585F, 1.005F);
		tepal6.addChild(cube_r980);
		setRotationAngle(cube_r980, 0.0F, 0.0873F, 0.5236F);
	}

	private void init164() {
		cube_r980.floatCubes.add(new FloatCube(0, 0, -0.0035F, 0.0071F, -0.605F, 4.03F, 0.0F, 0.6F, 0.0F, false));

		cube_r981 = new FlowerPart(this);
		cube_r981.setRotationPoint(6.0F, 0.0F, 0.6F);
		tepal6.addChild(cube_r981);
		setRotationAngle(cube_r981, 0.0F, 0.0F, 0.5236F);
		cube_r981.floatCubes.add(new FloatCube(0, 0, 0.635F, -1.037F, -1.0F, 4.0F, 0.0F, 0.8F, 0.0F, false));
		cube_r981.floatCubes.add(new FloatCube(0, 0, 4.135F, -1.037F, -1.07F, 0.5F, 0.0F, 0.94F, 0.0F, false));

		cube_r982 = new FlowerPart(this);
		cube_r982.setRotationPoint(3.0F, -2.0F, 0.0F);
		tepal6.addChild(cube_r982);
		setRotationAngle(cube_r982, 0.0F, 0.0F, -0.1745F);
		cube_r982.floatCubes.add(new FloatCube(0, 2, -1.3668F, 0.4311F, -1.0F, 1.5F, 0.0F, 2.0F, 0.0F, false));

		cube_r983 = new FlowerPart(this);
		cube_r983.setRotationPoint(1.77F, -1.05F, 1.015F);
		tepal6.addChild(cube_r983);
		setRotationAngle(cube_r983, 0.0F, -0.2618F, -0.1745F);
		cube_r983.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, -0.615F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r984 = new FlowerPart(this);
		cube_r984.setRotationPoint(1.77F, -1.05F, -1.015F);
		tepal6.addChild(cube_r984);
		setRotationAngle(cube_r984, 0.0F, 0.2618F, -0.1745F);
		cube_r984.floatCubes.add(new FloatCube(0, 0, -2.3955F, -0.2909F, 0.015F, 2.405F, 0.0F, 0.6F, 0.0F, false));

		cube_r985 = new FlowerPart(this);
		cube_r985.setRotationPoint(1.27F, -1.05F, -0.6F);
		tepal6.addChild(cube_r985);
		setRotationAngle(cube_r985, 0.0F, 0.0F, -0.1745F);
		cube_r985.floatCubes.add(new FloatCube(0, 0, -1.9031F, -0.2041F, 0.2F, 2.405F, 0.0F, 0.8F, 0.0F, false));

		stemal = new FlowerPart(this);
		stemal.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal);
		setRotationAngle(stemal, -0.4931F, -0.438F, -0.0571F);
		stemal.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r986 = new FlowerPart(this);
	}

	private void init165() {
		cube_r986.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal.addChild(cube_r986);
		setRotationAngle(cube_r986, 0.0F, 0.0F, 0.8814F);
		cube_r986.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r987 = new FlowerPart(this);
		cube_r987.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal.addChild(cube_r987);
		setRotationAngle(cube_r987, 0.0F, 0.0F, 0.2618F);
		cube_r987.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r988 = new FlowerPart(this);
		cube_r988.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal.addChild(cube_r988);
		setRotationAngle(cube_r988, 0.0F, 0.0F, 0.8814F);
		cube_r988.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r989 = new FlowerPart(this);
		cube_r989.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal.addChild(cube_r989);
		setRotationAngle(cube_r989, 0.0F, 0.0F, 0.5498F);
		cube_r989.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r990 = new FlowerPart(this);
		cube_r990.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal.addChild(cube_r990);
		setRotationAngle(cube_r990, 0.0F, 0.0F, 0.8814F);
		cube_r990.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal2 = new FlowerPart(this);
		stemal2.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal2);
		setRotationAngle(stemal2, -0.1017F, 0.0303F, 0.0497F);
		stemal2.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r991 = new FlowerPart(this);
		cube_r991.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal2.addChild(cube_r991);
		setRotationAngle(cube_r991, 0.0F, 0.0F, 0.8814F);
		cube_r991.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));
	}

	private void init166() {

		cube_r992 = new FlowerPart(this);
		cube_r992.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal2.addChild(cube_r992);
		setRotationAngle(cube_r992, 0.0F, 0.0F, 0.2618F);
		cube_r992.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r993 = new FlowerPart(this);
		cube_r993.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal2.addChild(cube_r993);
		setRotationAngle(cube_r993, 0.0F, 0.0F, 0.8814F);
		cube_r993.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r994 = new FlowerPart(this);
		cube_r994.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal2.addChild(cube_r994);
		setRotationAngle(cube_r994, 0.0F, 0.0F, 0.5498F);
		cube_r994.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r995 = new FlowerPart(this);
		cube_r995.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal2.addChild(cube_r995);
		setRotationAngle(cube_r995, 0.0F, 0.0F, 0.8814F);
		cube_r995.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal3 = new FlowerPart(this);
		stemal3.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal3);
		setRotationAngle(stemal3, 0.1582F, 0.2804F, -0.7053F);
		stemal3.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r996 = new FlowerPart(this);
		cube_r996.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal3.addChild(cube_r996);
		setRotationAngle(cube_r996, 0.0F, 0.0F, 0.8814F);
		cube_r996.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r997 = new FlowerPart(this);
		cube_r997.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal3.addChild(cube_r997);
	}

	private void init167() {
		setRotationAngle(cube_r997, 0.0F, 0.0F, 0.2618F);
		cube_r997.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r998 = new FlowerPart(this);
		cube_r998.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal3.addChild(cube_r998);
		setRotationAngle(cube_r998, 0.0F, 0.0F, 0.8814F);
		cube_r998.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r999 = new FlowerPart(this);
		cube_r999.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal3.addChild(cube_r999);
		setRotationAngle(cube_r999, 0.0F, 0.0F, 0.5498F);
		cube_r999.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1000 = new FlowerPart(this);
		cube_r1000.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal3.addChild(cube_r1000);
		setRotationAngle(cube_r1000, 0.0F, 0.0F, 0.8814F);
		cube_r1000.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal5 = new FlowerPart(this);
		stemal5.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal5);
		setRotationAngle(stemal5, -0.2949F, -0.1812F, -0.7237F);
		stemal5.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1001 = new FlowerPart(this);
		cube_r1001.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal5.addChild(cube_r1001);
		setRotationAngle(cube_r1001, 0.0F, 0.0F, 0.8814F);
		cube_r1001.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r1002 = new FlowerPart(this);
		cube_r1002.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal5.addChild(cube_r1002);
		setRotationAngle(cube_r1002, 0.0F, 0.0F, 0.2618F);
		cube_r1002.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1003 = new FlowerPart(this);
	}

	private void init168() {
		cube_r1003.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal5.addChild(cube_r1003);
		setRotationAngle(cube_r1003, 0.0F, 0.0F, 0.8814F);
		cube_r1003.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1004 = new FlowerPart(this);
		cube_r1004.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal5.addChild(cube_r1004);
		setRotationAngle(cube_r1004, 0.0F, 0.0F, 0.5498F);
		cube_r1004.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1005 = new FlowerPart(this);
		cube_r1005.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal5.addChild(cube_r1005);
		setRotationAngle(cube_r1005, 0.0F, 0.0F, 0.8814F);
		cube_r1005.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal6 = new FlowerPart(this);
		stemal6.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal6);
		setRotationAngle(stemal6, 0.3149F, 0.1775F, -0.2915F);
		stemal6.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1006 = new FlowerPart(this);
		cube_r1006.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal6.addChild(cube_r1006);
		setRotationAngle(cube_r1006, 0.0F, 0.0F, 0.8814F);
		cube_r1006.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r1007 = new FlowerPart(this);
		cube_r1007.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal6.addChild(cube_r1007);
		setRotationAngle(cube_r1007, 0.0F, 0.0F, 0.2618F);
		cube_r1007.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1008 = new FlowerPart(this);
		cube_r1008.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal6.addChild(cube_r1008);
		setRotationAngle(cube_r1008, 0.0F, 0.0F, 0.8814F);
		cube_r1008.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));
	}

	private void init169() {

		cube_r1009 = new FlowerPart(this);
		cube_r1009.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal6.addChild(cube_r1009);
		setRotationAngle(cube_r1009, 0.0F, 0.0F, 0.5498F);
		cube_r1009.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1010 = new FlowerPart(this);
		cube_r1010.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal6.addChild(cube_r1010);
		setRotationAngle(cube_r1010, 0.0F, 0.0F, 0.8814F);
		cube_r1010.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));

		stemal4 = new FlowerPart(this);
		stemal4.setRotationPoint(0.0F, -1.0F, 0.0F);
		perianth.addChild(stemal4);
		setRotationAngle(stemal4, 0.0F, 0.0F, -0.9163F);
		stemal4.floatCubes.add(new FloatCube(0, 0, -0.2F, -3.0F, 0.0F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1011 = new FlowerPart(this);
		cube_r1011.setRotationPoint(7.105F, -13.29F, 0.145F);
		stemal4.addChild(cube_r1011);
		setRotationAngle(cube_r1011, 0.0F, 0.0F, 0.8814F);
		cube_r1011.floatCubes.add(new FloatCube(0, 0, -0.005F, -0.055F, -0.09F, 0.1F, 0.115F, 0.1F, 0.0F, false));

		cube_r1012 = new FlowerPart(this);
		cube_r1012.setRotationPoint(-0.085F, -3.005F, 0.09F);
		stemal4.addChild(cube_r1012);
		setRotationAngle(cube_r1012, 0.0F, 0.0F, 0.2618F);
		cube_r1012.floatCubes.add(new FloatCube(0, 0, -0.105F, -2.94F, -0.09F, 0.2F, 3.0F, 0.2F, 0.0F, false));

		cube_r1013 = new FlowerPart(this);
		cube_r1013.setRotationPoint(3.28F, -10.06F, 0.09F);
		stemal4.addChild(cube_r1013);
		setRotationAngle(cube_r1013, 0.0F, 0.0F, 0.8814F);
		cube_r1013.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1014 = new FlowerPart(this);
		cube_r1014.setRotationPoint(0.69F, -5.86F, 0.09F);
		stemal4.addChild(cube_r1014);
	}

	private void init170() {
		setRotationAngle(cube_r1014, 0.0F, 0.0F, 0.5498F);
		cube_r1014.floatCubes.add(new FloatCube(0, 0, -0.105F, -4.94F, -0.09F, 0.2F, 5.0F, 0.2F, 0.0F, false));

		cube_r1015 = new FlowerPart(this);
		cube_r1015.setRotationPoint(7.22F, -13.31F, 0.09F);
		stemal4.addChild(cube_r1015);
		setRotationAngle(cube_r1015, 0.0F, 0.0F, 0.8814F);
		cube_r1015.floatCubes.add(new FloatCube(0, 0, -0.105F, -0.11F, -0.09F, 0.2F, 0.17F, 0.2F, 0.0F, false));
	}


	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		stem.render(f5);
		flower.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	// ---- Accessors added for the branch/skill-tree renderer ----
	// The flower has 6 florets ("perianth" groups), one per branch.
	// Order here is arbitrary but fixed: index = branch slot.
	public ModelRenderer getStem() {
		return stem;
	}

	// The flower head is NOT a child of the stem - it's a separate root part that
	// must be positioned/rotated manually. It has two halves, each carrying 3 florets.
	public ModelRenderer getFlower() {
		return flower;
	}

	public ModelRenderer getHalf1() {
		return half1;
	}

	public ModelRenderer getHalf2() {
		return half2;
	}

	// index 0-2 belong under half1, index 3-5 under half2 - call postRender on the
	// matching half BEFORE rendering these, or positions/rotations will be wrong.
	public ModelRenderer[] getHalf1Florets() {
		return new ModelRenderer[] { perianth4, perianth5, perianth6 };
	}

	public ModelRenderer[] getHalf2Florets() {
		return new ModelRenderer[] { perianth3, perianth2, perianth };
	}
}