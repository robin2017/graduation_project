# -*- coding: utf-8 -*-
from pylab import *
import networkx as nx
import os
os.chdir('/Users/robin/workspace/graduation_project/DATA/part2/HashTopologyHandle')
filename='result.txt'
G1=nx.Graph()
G2=nx.Graph()
G3=nx.Graph()

with open(filename) as file:
    for line in file:
        for x in line.split():
            print x
        print '---end---'
        head, tail ,w= [str(x) for x in line.split()]
        if float(w)>=0.999:
            print head, tail,w,'rr'
            G1.add_edge(head,tail,weight=w)
            G1.add_node(head)
            G1.add_node(tail)
        elif float(w)>=0.9:
            print head, tail,w,'r'
            G2.add_edge(head,tail,weight=w)
            G1.add_node(head)
            G1.add_node(tail)
        elif float(w)>=0.4:
            print head, tail,w,'b'
            G3.add_edge(head,tail,weight=w)
            G1.add_node(head)
            G1.add_node(tail)


pos=nx.spring_layout(G1)


nx.draw(G1,pos,node_color='y',with_labels=True,node_size=70,edge_color='r',width=3)
nx.draw(G2,pos,node_color='y',with_labels=True,node_size=70,edge_color='r')
nx.draw(G3,pos,node_color='y',with_labels=True,node_size=70,edge_color='b')
plt.show()
