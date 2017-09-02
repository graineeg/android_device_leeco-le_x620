/*****************************************************************************
*  Copyright Statement:
*  --------------------
*  This software is protected by Copyright and the information contained
*  herein is confidential. The software may not be copied and the information
*  contained herein may not be used or disclosed except with the written
*  permission of MediaTek Inc. (C) 2008
*
*  BY OPENING THIS FILE, BUYER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
*  THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
*  RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO BUYER ON
*  AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
*  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
*  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
*  NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
*  SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
*  SUPPLIED WITH THE MEDIATEK SOFTWARE, AND BUYER AGREES TO LOOK ONLY TO SUCH
*  THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. MEDIATEK SHALL ALSO
*  NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE RELEASES MADE TO BUYER'S
*  SPECIFICATION OR TO CONFORM TO A PARTICULAR STANDARD OR OPEN FORUM.
*
*  BUYER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND CUMULATIVE
*  LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
*  AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
*  OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY BUYER TO
*  MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
*
*  THE TRANSACTION CONTEMPLATED HEREUNDER SHALL BE CONSTRUED IN ACCORDANCE
*  WITH THE LAWS OF THE STATE OF CALIFORNIA, USA, EXCLUDING ITS CONFLICT OF
*  LAWS PRINCIPLES.  ANY DISPUTES, CONTROVERSIES OR CLAIMS ARISING THEREOF AND
*  RELATED THERETO SHALL BE SETTLED BY ARBITRATION IN SAN FRANCISCO, CA, UNDER
*  THE RULES OF THE INTERNATIONAL CHAMBER OF COMMERCE (ICC).
*
*  modified by:daniel_hk(https://github.com/daniel_hk)
*	terminate raw_buf with 0
*	add MTK_IMS_SUPPORT and MTK_VOLTE_SUPPORT properties
*****************************************************************************/
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <cutils/properties.h>
#include <android/log.h>
#include <sys/ioctl.h>
#include <errno.h>
#include <stdlib.h>


//----------------debug log define-----------------//
#define LOGD(...)	do{ __android_log_print(ANDROID_LOG_DEBUG, "ccci_lib", __VA_ARGS__); }while(0)
#define LOGE(...)	do{ __android_log_print(ANDROID_LOG_ERROR, "ccci_lib", __VA_ARGS__); }while(0)

#define MTK_TELEPHONY_BOOTUP_MODE_SLOT1	0
#define MTK_TELEPHONY_BOOTUP_MODE_SLOT2	1
#define MTK_IMS_SUPPORT		"ro.mtk_ims_support"
#define MTK_VOLTE_SUPPORT	"ro.mtk_volte_support"

static int parse_info(char raw_data[], int raw_size, char name[], char val[], int size)
{
	int i,j;
	char tmp_name[32];
	char tmp_val[32];
	int state = 0;

	LOGD("parse_info name:%s", name);

	for(i=0; i<raw_size; i++) {
		switch(state) {
		case 0: // Init
			if(raw_data[i] == '[') {
				j=0;
				state = 1;
			}
			break;

		case 1: // Read name
			if(raw_data[i] == ']') {
				state =2; // name parse done
				tmp_name[j] = '\0';
				j = 0;
			} else if((raw_data[i] == '\r')||(raw_data[i] == '\n')) {
				j = 0;
				state = 0;
			} else {
				tmp_name[j] = raw_data[i];
				j++;
			}
			break;

		case 2: // Get ':'
			if(raw_data[i] == ':') {
				state = 3; //divider found
				tmp_val[0] = 0;
			} else
				state = 0; // Invalid format
			break;

		case 3: // Read value
			if((raw_data[i] == '\r')||(raw_data[i] == '\n')) {
				state =4; // value parse done
				tmp_val[j]='\0';
				j = 0;
			} else {
				tmp_val[j] = raw_data[i];
				j++;
				break;
			}

		case 4: // Check if name is match
			if(strcmp(tmp_name, name)==0) {
				// Copy value
				snprintf(val, size, "%s", tmp_val);
				return 0;
			}
			state = 0;
			break;
		default:
			break;
		}
	}
	return -1;
}

int query_kcfg_setting(char name[], char val[], int size)
{
	char *raw_buf;
	int fd;
	int ret;

	fd = open("/sys/kernel/ccci/kcfg_setting", O_RDONLY);
	if(fd < 0) {
		LOGE("open sys file fail(%d)", errno);
		return -1;
	}
	raw_buf = (char*)malloc(4096);
	if(NULL == raw_buf) {
		LOGE("allock memory fail");
		close(fd);
		return -2;
	}
	ret = read(fd, raw_buf, 4096);
	if(ret <= 0) {
		LOGE("read info fail ret%d(%d)", ret, errno);
		ret = -3;
		goto _Exit;
	}
	if (ret == 4096) ret--;
	raw_buf[ret] = '\0';		// discard residues in raw_buf
	LOGD("read info:%s", raw_buf);

	//------------------------------------------------
	ret = parse_info(raw_buf, ret, name, val, size);
	//------------------------------------------------

_Exit:
	free(raw_buf);
	close(fd);

	return ret;
}

int query_prj_cfg_setting(char name[], char val[], int size)
{
	char value[PROPERTY_VALUE_MAX];
//-- For MTK_TELEPHONY_BOOTUP_MODE_SLOT1
#ifdef MTK_TELEPHONY_BOOTUP_MODE_SLOT1
	if(strcmp(name, "BOOTUP_MODE_SLOT1")==0) {
		snprintf(val, size, "%s", MTK_TELEPHONY_BOOTUP_MODE_SLOT1);
		return 0;
	}
#endif

//-- For MTK_TELEPHONY_BOOTUP_MODE_SLOT2
#ifdef MTK_TELEPHONY_BOOTUP_MODE_SLOT2
	if(strcmp(name, "BOOTUP_MODE_SLOT2")==0) {
		snprintf(val, size, "%s", MTK_TELEPHONY_BOOTUP_MODE_SLOT2);
		return 0;
	}
#endif

//-- For MTK_IMS_SUPPORT 
	if(property_get(MTK_IMS_SUPPORT, value, NULL) > 0) {
		if(strstr("yes,ture,on", value) > 0) {
			if(strcmp(name, "MTK_IMS_SUPPORT")==0) {
				snprintf(val, size, "1");
				return 0;
			}
		}
	}

//-- For MTK_VOLTE_SUPPORT
	if(property_get(MTK_VOLTE_SUPPORT, value, NULL) > 0) {
		if(strstr("yes,ture,on", value) > 0) {
			if(strcmp(name, "MTK_VOLTE_SUPPORT")==0) {
				snprintf(val, size, "1");
				return 0;
			}
		}
	}

	return -1;
}


