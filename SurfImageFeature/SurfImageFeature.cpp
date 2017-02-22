#include "StdAfx.h"
#include <stdio.h>  
#include <iostream>  
#include "opencv2/core/core.hpp"  
#include "opencv2/features2d/features2d.hpp"  
#include "opencv2/highgui/highgui.hpp"  
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/nonfree/features2d.hpp>
#include <opencv2/legacy/legacy.hpp>
#include<iostream>
#include<Windows.h>
#include<io.h>
#include<fstream>
using namespace std;
using namespace cv;

int split(const string& str, vector<string>& ret_, string sep = ",")
{
    if (str.empty())
    {
        return 0;
    }

    string tmp;
    string::size_type pos_begin = str.find_first_not_of(sep);
    string::size_type comma_pos = 0;

    while (pos_begin != string::npos)
    {
        comma_pos = str.find(sep, pos_begin);
        if (comma_pos != string::npos)
        {
            tmp = str.substr(pos_begin, comma_pos - pos_begin);
            pos_begin = comma_pos + sep.length();
        }
        else
        {
            tmp = str.substr(pos_begin);
            pos_begin = comma_pos;
        }

        if (!tmp.empty())
        {
            ret_.push_back(tmp);
            tmp.clear();
        }
    }
    return 0;
}
float stringToNum(const string& str){
	istringstream iss(str);
	float num;
	iss>>num;
	return num;
}

string doubleToString(double d){
	ostringstream oss;
	oss<<d;
	string str=oss.str();
	return str;
}

void saveFeature(std::vector<KeyPoint> keypoints,const char* filename){
	ofstream out(filename);
	for(int i=0;i<keypoints.size();i++){
		out<<keypoints.at(i).pt.x<<";";
		out<<keypoints.at(i).pt.y<<";";
		out<<keypoints.at(i).size<<";";
		out<<keypoints.at(i).angle<<";";
		out<<keypoints.at(i).response<<";";
		out<<keypoints.at(i).octave<<";";
		out<<keypoints.at(i).class_id<<";";
		out<<endl;
	}
	out.close();
}

std::vector<KeyPoint> ReadFeature(const char* name){
	vector<KeyPoint> vecto;
	ifstream in(name);
	char buffer[5512];
	while(!in.eof()){
		KeyPoint ip;
		in.getline(buffer,5512);
		string str(buffer);
		if(str.length()==0)
			break;
		vector<string> vec;
		split(str,vec,";");
		ip.pt.x=stringToNum(vec.at(0));
		ip.pt.y=stringToNum(vec.at(1));
		ip.size=stringToNum(vec.at(2));
		ip.angle=stringToNum(vec.at(3));
		ip.response=stringToNum(vec.at(4));
		ip.octave=stringToNum(vec.at(5));
		ip.class_id=stringToNum(vec.at(6));
		vecto.push_back(ip);
	}
	return vecto;
}

int drawFeature(const char* picname,const char* filename){
	Mat img = imread(picname);  
    if (!img.data ) { printf("读取图片image0错误~！ \n"); return false; }  
    //【2】利用SURF检测器检测的关键点  
    int minHessian = 300;  
    SURF detector(minHessian);  
    std::vector<KeyPoint> keypoints;  
    detector.detect(img, keypoints);  
	saveFeature(keypoints,filename);
}

void _1_drawFeature(string picFolderPath,string dataFolderPath,ostream &fout=cout){
	CreateDirectoryA(dataFolderPath.c_str(),NULL);
	int num=0;
    _finddata_t FileInfo;
    string strfind = picFolderPath + "\\*";
    long Handle = _findfirst(strfind.c_str(), &FileInfo);
    if (Handle == -1L)
    {
        cerr << "can not match the folder path" << endl;
        exit(-1);
    }
    do{
        //判断是否有子目录
        if (FileInfo.attrib & _A_SUBDIR)    
        {
        }
        else  
        {
			num++;
			string imgpath=picFolderPath+  "\\"+  FileInfo.name;
            fout <<num<<" ---------  "<<imgpath<<endl;
			string datapath=dataFolderPath+  "\\"+  FileInfo.name+".txt";
			drawFeature(imgpath.c_str(),datapath.c_str());
        }
    }while (_findnext(Handle, &FileInfo) == 0);
    _findclose(Handle);
}


double get_true_min(std::vector<double> arr){
	const double MIN_STD=0.05;   //对比发现0.05更加的合适
	sort(arr.begin(),arr.end());
	//先求基本斜率
	double start=arr[arr.size()/4];
	double end=arr[arr.size()/4*3];
	double base_rate=(end-start)/(arr.size()/2);
	double period_rate[4];
	int range=arr.size()/2;
	while(range>arr.size()/10){
		period_rate[0]=(arr[range/4]-arr[0])/(range/4);
		period_rate[1]=(arr[range/4*2]-arr[range/4])/(range/4);
		period_rate[2]=(arr[range/4*3]-arr[range/4*2])/(range/4);
		period_rate[3]=(arr[range]-arr[range/4*3])/(range/4);
		double total=0;
		int cnt=0;
		for(int i=0;i<4;i++)
			if(period_rate[i]<2*base_rate){
				cnt++;
				total+=period_rate[i];
			}
		base_rate=(total )/(cnt);  
		range/=2;
	}
	range*=2;
	int index=0;
	double pre_tmp,cur_tmp;
	for(int i=range;i>0;i--){
		cur_tmp=(arr[i]-(arr[range]-(range-i)*base_rate))/arr[i];
		cout<<i<<"    "<<cur_tmp<<endl;
		if(cur_tmp<-0.05&&cur_tmp<2*pre_tmp&&pre_tmp<0){    //这里的参数需要调试的
			index=i+1;
			break;
		}
		pre_tmp=cur_tmp;
	}
	 return arr[index]>MIN_STD?MIN_STD:arr[index];
}
double getRatioFromKeyPoint_fault(std::vector<KeyPoint> kp1,std::vector<KeyPoint> kp2){
	//【3】计算描述符（特征向量）  
	Mat img;
    SURF extractor;  
    Mat descriptors_1, descriptors_2;  
    extractor.compute(img, kp1, descriptors_1);  
    extractor.compute(img, kp2, descriptors_2);  
    //【4】采用FLANN算法匹配描述符向量  
    FlannBasedMatcher matcher;  
    std::vector< DMatch > matches;  
    matcher.match(descriptors_1, descriptors_2, matches);  
    double max_dist = 0; double min_dist = 100;  
    //【6】存下符合条件的匹配结果（即其距离小于2* min_dist的），使用radiusMatch同样可行  
    std::vector< DMatch > good_matches;  
	std::vector<double> tmp; 
	ofstream outt("out.txt");
 
	for(vector< DMatch >::iterator it=matches.begin();it!=matches.end();it++){
		outt<<((*it).distance)<<endl;
		tmp.push_back((*it).distance);
	}
 
	double true_min=get_true_min(tmp);
    for (int i = 0; i < descriptors_1.rows; i++)  
    {  
        if (matches[i].distance <2*true_min)  
        {  
            good_matches.push_back(matches[i]); 
        }  
    }  
    //【8】输出相关匹配点信息  
    for (int i = 0; i < good_matches.size(); i++)  
    {  
        printf(">符合条件的匹配点 [%d] 特征点1: %d  -- 特征点2: %d  \n", i, good_matches[i].queryIdx, good_matches[i].trainIdx);  
    }  
    return 0;  
}

double getRatioFromPic(const char* name1,const char* name2){

    Mat img_1 = imread(name1);  
    Mat img_2 = imread(name2);  
    if (!img_1.data || !img_2.data) { printf("读取图片image0错误~！ \n"); return false; }  
  
    //【2】利用SURF检测器检测的关键点  
    int minHessian = 300;  
    SURF detector(minHessian);  
    std::vector<KeyPoint> keypoints_1, keypoints_2;  
    detector.detect(img_1, keypoints_1);  
    detector.detect(img_2, keypoints_2);  
  
    //【3】计算描述符（特征向量）  
    SURF extractor;  
    Mat descriptors_1, descriptors_2;  
    extractor.compute(img_1, keypoints_1, descriptors_1);  
    extractor.compute(img_2, keypoints_2, descriptors_2);  
    //【4】采用FLANN算法匹配描述符向量  
    FlannBasedMatcher matcher;  
    std::vector< DMatch > matches;  
    matcher.match(descriptors_1, descriptors_2, matches);  
    double max_dist = 0; double min_dist = 100;  
    //【6】存下符合条件的匹配结果（即其距离小于2* min_dist的），使用radiusMatch同样可行  
    std::vector< DMatch > good_matches;  
	std::vector<double> tmp; 
	ofstream outt("out.txt");
 
	for(vector< DMatch >::iterator it=matches.begin();it!=matches.end();it++){
		outt<<((*it).distance)<<endl;
		tmp.push_back((*it).distance);
	}
 
	double true_min=get_true_min(tmp);
    for (int i = 0; i < descriptors_1.rows; i++)  
    {  
        if (matches[i].distance <=2*true_min)  
        {  
            good_matches.push_back(matches[i]); 
        }  
    }  



	// //【7】绘制出符合条件的匹配点  
 //   Mat img_matches;  
 //   drawMatches(img_1, keypoints_1, img_2, keypoints_2,  
 //       good_matches, img_matches, Scalar::all(-1), Scalar::all(-1),  
 //       vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);  
 //   //【8】输出相关匹配点信息  
 //   for (int i = 0; i < good_matches.size(); i++)  
 //   {  
 //       printf(">符合条件的匹配点 [%d] 特征点1: %d  -- 特征点2: %d  \n", i, good_matches[i].queryIdx, good_matches[i].trainIdx);  
 //   }  
 //   //【9】显示效果图  
 //   imshow("匹配效果图", img_matches);  
	//waitKey(0); 
	


	double result=good_matches.size()*1.0/(keypoints_1.size()>keypoints_2.size()?keypoints_1.size():keypoints_2.size());
    return result;  

}

string getFlag(string path){
	int first=path.find_last_of('\\');
	int last=path.find_last_of('.');
	return path.substr(first+1,last-first-1);
}

int _2_compareRatio(string aimfile,string folder,string resultpath,ostream &fout=cout){
	CreateDirectoryA(resultpath.c_str(),NULL); 
	int num=0;
	vector<string> vec_res;
	vector<string> vec_res_app;
    _finddata_t FileInfo;

    string strfind = folder + "\\*";
    long Handle = _findfirst(strfind.c_str(), &FileInfo);
    if (Handle == -1L)
    {
        cerr << "can not match the folder path" << endl;
        exit(-1);
    }
    do{
        //判断是否有子目录
        if (FileInfo.attrib & _A_SUBDIR)    
        {
        }
        else  
        {
			num++;
			string imgpath=aimfile;
            fout <<num<<" ---------  "<<imgpath<<endl;
			string comparepath=folder+  "\\"+  FileInfo.name;
			double res=getRatioFromPic(imgpath.c_str(),comparepath.c_str());
			vec_res.push_back(getFlag(imgpath)+"  "+getFlag(comparepath)+"  "+doubleToString(res));
			if(res>0.1)
				vec_res_app.push_back(getFlag(imgpath)+"  "+getFlag(comparepath)+"  "+doubleToString(res));

        }
    }while ( _findnext(Handle, &FileInfo) == 0);


	ofstream out;
	out.open(resultpath+"\\"+getFlag(aimfile)+".txt",std::ofstream::out);

	ofstream out_app;
	out_app.open(resultpath+"\\total.txt",std::ofstream::out|std::ofstream::app);

	for(int i=0;i<vec_res.size();i++){
		out<<vec_res.at(i)<<endl;
	}

	for(int i=0;i<vec_res_app.size();i++){
		out_app<<vec_res_app.at(i)<<endl;
	}


	out.close();
	out_app.close();
	_findclose(Handle);
}


int _3_batCompareRatio(string folder,string resultpath,ostream &fout=cout){
	CreateDirectoryA(resultpath.c_str(),NULL); 
	int num=0;
	vector<string> vec_res;
    _finddata_t FileInfo;
    string strfind = folder + "\\*";
    long Handle = _findfirst(strfind.c_str(), &FileInfo);
    if (Handle == -1L)
    {
        cerr << "can not match the folder path" << endl;
        exit(-1);
    }
    do{
        //判断是否有子目录
        if (FileInfo.attrib & _A_SUBDIR)    
        {
        }
        else  
        {
			num++;
			string aimpath=folder+  "\\"+  FileInfo.name;
			_2_compareRatio(aimpath,folder,resultpath);
        }
    }while ( _findnext(Handle, &FileInfo) == 0);
    _findclose(Handle);
}


int _tmain(int argc, _TCHAR* argv[])
{
	//drawFeature("weich1.jpg","weich1.txt");
	//_1_drawFeature("d:\\a_pic\\test_tmp\\test_social_pic","d:\\a_pic\\test_tmp\\ddd1");
	 // getRatioFromPic("d:\\a_pic\\test_tmp\\test_social_pic\\1_1_2.png","d:\\a_pic\\test_tmp\\test_social_pic\\1_1_5.png");

	// _2_compareRatio("d:\\a_pic\\test_tmp\\test_social_pic\\1_1_2.png","d:\\a_pic\\test_tmp\\test_social_pic","d:\\a_pic\\test_tmp\\ddd_res");

	_3_batCompareRatio("d:\\a_pic\\test_tmp\\test_social_pic","d:\\a_pic\\test_tmp\\result_194");
	return 0;
}

