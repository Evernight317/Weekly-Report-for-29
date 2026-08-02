#include <stdio.h>
#include <stdlib.h>

#ifndef ElemType
#define ElemType int
#endif

#define MAXSIZE 100

typedef struct {
	ElemType data[MAXSIZE];
	int length;
} Sqlist;

void InitList(Sqlist *L){
	L->length = 0;
}
int Length(Sqlist *L){
	return L->length;
}
int LocateElem(Sqlist *L, ElemType e){
	for(int i = 0; i < L->length; i++){
		if(L->data[i] == e){
			return i + 1;
		}
	}
	return 0;
}
int GetElem(Sqlist *L, int i, ElemType *e){
	if(i < 1 || i > L->length){
		return 0;
	}
	*e = L->data[i - 1];
	return 1;
}
void ListInsert(Sqlist *L, int i, ElemType e){
	if(i < 1 || i > L->length + 1){
		return;
	}
	if(L->length >= MAXSIZE){
		return;
	}
	for(int j = L->length - 1; j >= i - 1; j--){
		L->data[j + 1] = L->data[j];
	}
	L->data[i - 1] = e;
	L->length++;
}
void ListDelete(Sqlist *L, int i, ElemType *e){
	if(i < 1 || i > L->length){
		return;
	}
	*e = L->data[i - 1];
	for(int j = i; j < L->length; j++){
		L->data[j - 1] = L->data[j];
	}
	L->length--;
}
void PrintList(Sqlist *L){
	for(int i = 0; i < L->length; i++){
		printf("%d ", L->data[i]);
	}
	printf("\n");
}
int Empty(Sqlist *L){
	return L->length == 0;
}
void DestroyList(Sqlist *L){
	L->length = 0;
}
int main(){
	Sqlist L;
	InitList(&L);
	return 0;
}