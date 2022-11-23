import pandas as pd
from matplotlib import pyplot as plt
import numpy as np
import seaborn as sns
import sys

columns = ["size", "time"]
df = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_times.csv', header=None, names=columns)
df["size"] = df["size"]**2

df2 = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_threads_times.csv', header=None, names=columns)
df2["size"] = df2["size"]**2

df3 = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_pools_create_cont_times.csv', header=None, names=columns)
df3["size"] = df3["size"]**2

df4 = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_pools_create_once_times.csv', header=None, names=columns)
df4["size"] = df4["size"]**2

sns.set(font_scale=1.5,style='ticks')
fig, ax = plt.subplots()
#ax.set(xscale="log", yscale="log")

ax.set(xscale="log")

plt.rcParams["figure.figsize"] = [14, 8]
plt.rcParams['font.size'] = '15'
plt.rcParams['lines.linewidth'] = '1'
plt.rcParams['lines.markersize'] = '15'
plt.rcParams["figure.autolayout"] = True

g1 = sns.regplot(x = 'size', y= 'time', data=df.iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="single-thr")

sns.regplot(x = 'size', y= 'time', data=df2.iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="multi-thr")
sns.regplot(x = 'size', y= 'time', data=df3.iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="pools-cont")
sns.regplot(x = 'size', y= 'time', data=df4.iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="pools-once")
columns = ["size","block_size", "time"]
df = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_pools_block_size_times.csv', header=None, names=columns)
df["size"] = df["size"]**2
sns.regplot(x = 'size', y= 'time',data=df[df["block_size"] == 8].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="block-size 8")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 16].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="block-size 16")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 32].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="block-size 32")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 64].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="block-size 64")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 128].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="block-size 128")

g1.set_xlabel('size [px]')
g1.set_ylabel('time [ms]')
plt.legend()
plt.show()