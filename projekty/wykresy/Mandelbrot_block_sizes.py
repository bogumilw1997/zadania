import pandas as pd
from matplotlib import pyplot as plt
import numpy as np
import seaborn as sns
import sys

columns = ["size","block_size", "time"]
df = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_pools_block_size_times.csv', header=None, names=columns)
df["size"] = df["size"]**2

sns.set(font_scale=1.5,style='ticks')
fig, ax = plt.subplots()
#ax.set(xscale="log", yscale="log")

ax.set(xscale="log")

plt.rcParams["figure.figsize"] = [14, 8]
plt.rcParams['font.size'] = '15'
plt.rcParams['lines.linewidth'] = '1'
plt.rcParams['lines.markersize'] = '15'
plt.rcParams["figure.autolayout"] = True

g1 = sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 4].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="4")

sns.regplot(x = 'size', y= 'time',data=df[df["block_size"] == 8].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="8")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 16].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="16")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 32].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="32")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 64].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="64")
sns.regplot(x = 'size', y= 'time', data=df[df["block_size"] == 128].iloc[1: , :], ax=ax, marker="o", order=1, x_estimator=np.mean, ci=95, label="128")

g1.set_xlabel('size [px]')
g1.set_ylabel('time [ms]')
plt.legend()
plt.show()