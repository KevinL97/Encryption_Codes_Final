#Stablish the directory path

setwd("C:/Users/User/Desktop/Cifradores/AES/AES_modificado")
#setwd("C:/Users/User/Desktop/Cifradores/RSA")
#setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#/////////////////////TIME EXECUTION////////////////////////////////
#AES algorithm

#ECB mode
aes_ecb_time <- read.delim2("tiempos_ECB.txt", header = FALSE, sep = ",")

colnames(aes_ecb_time) <- c("Encryption", "Decryption")

ecb_eTime <- aes_ecb_time$Encryption
ecb_dTime <- aes_ecb_time$Decryption

ecb_eTime <- as.numeric(ecb_eTime)
ecb_dTime <- as.numeric(ecb_dTime)

#CBC mode
aes_cbc_time <- read.delim2("tiempos_CBC.txt", header = FALSE, sep = ",")

colnames(aes_cbc_time) <- c("Encryption", "Decryption")

cbc_eTime <- aes_cbc_time$Encryption
cbc_dTime <- aes_cbc_time$Decryption

cbc_eTime <- as.numeric(cbc_eTime)
cbc_dTime <- as.numeric(cbc_dTime)

text_length <- c(446,1100,2142,2972,3686)

#Comparing ECB vs CBC modes
mixed_aesmodes_Time <- data.frame(c1 = ecb_eTime, 
                            c2 = cbc_eTime,
                            c3 = text_length)

colnames(mixed_aesmodes_Time) <- c("ECB_encTime","CBC_encTime", "Text_length")


library(ggplot2)

p <- ggplot(mixed_aesmodes_Time, aes(x = Text_length)) +
  geom_point(aes(y = ECB_encTime), size = 4, color = "darkgoldenrod", shape=7) +
  geom_point(aes(y = CBC_encTime), size = 4, color = "deeppink", shape=1) +
  geom_line(aes(y = ECB_encTime, color = "ECB_encTime", group = 1), linewidth = 1) +
  geom_line(aes(y = CBC_encTime, color = "CBC_encTime", group = 1), linewidth = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "Time (ms)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.9),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
p+scale_color_manual(values=c("deeppink", "darkgoldenrod")) + 
  theme(panel.background = element_rect(fill = "white"))

  
#RSA algorithm

#1024 bits
setwd("C:/Users/User/Desktop/Cifradores/RSA")

rsa_1024_time <- read.delim2("time_1024.txt", header = FALSE, sep = ",")

colnames(rsa_1024_time) <- c("Encryption", "Decryption")

rsa_1024_eTime <- rsa_1024_time$Encryption
rsa_1024_dTime <- rsa_1024_time$Decryption

rsa_1024_eTime <- as.numeric(rsa_1024_eTime)
rsa_1024_dTime <- as.numeric(rsa_1024_dTime)


#2048 bits
rsa_2048_time <- read.delim2("time_2048.txt", header = FALSE, sep = ",")

colnames(rsa_2048_time) <- c("Encryption", "Decryption")

rsa_2048_eTime <- rsa_2048_time$Encryption
rsa_2048_dTime <- rsa_2048_time$Decryption

rsa_2048_eTime <- as.numeric(rsa_2048_eTime)
rsa_2048_dTime <- as.numeric(rsa_2048_dTime)


#3072
rsa_3072_time <- read.delim2("time_3072.txt", header = FALSE, sep = ",")

colnames(rsa_3072_time) <- c("Encryption", "Decryption")

rsa_3072_eTime <- rsa_3072_time$Encryption
rsa_3072_dTime <- rsa_3072_time$Decryption

rsa_3072_eTime <- as.numeric(rsa_3072_eTime)
rsa_3072_dTime <- as.numeric(rsa_3072_dTime)

text_length_rsa <- c(99, 242, 354)

#Comparing encryption time
mixed_rsaKey_Time <- data.frame(c1 = rsa_1024_eTime, 
                                  c2 = rsa_2048_eTime,
                                  c3 = rsa_3072_eTime)

colnames(mixed_rsaKey_Time) <- c("RSA_1024_encTime","RSA_2048_encTime", "RSA_3072_encTime")


library(ggplot2)

q <- ggplot(mixed_rsaKey_Time, aes(x = factor(1:nrow(mixed_rsaKey_Time)))) +
  geom_point(aes(y = RSA_1024_encTime), size = 4, color = "seagreen", shape=11) +
  geom_point(aes(y = RSA_2048_encTime), size = 4, color = "orangered", shape=5) +
  geom_point(aes(y = RSA_3072_encTime), size = 4, color = "mediumpurple", shape=0) +
  geom_line(aes(y = RSA_1024_encTime, color = "RSA_1024_encTime", group = 1), size = 1) +
  geom_line(aes(y = RSA_2048_encTime, color = "RSA_2048_encTime", group = 1), size = 1) +
  geom_line(aes(y = RSA_3072_encTime, color = "RSA_3072_encTime", group = 1), size = 1) +
  #scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Executions", y = "Time (ms)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.85, 0.6),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
q+scale_color_manual(values=c("seagreen", "orangered", "mediumpurple")) + 
  theme(panel.background = element_rect(fill = "white"))




#ECC algorithm
setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#E-1174
ecc_E1174_time <- read.delim2("time_E-1174.txt", header = FALSE, sep = ",")

colnames(ecc_E1174_time) <- c("Encryption", "Decryption")

E1174_eTime <- ecc_E1174_time$Encryption
E1174_dTime <- ecc_E1174_time$Decryption

E1174_eTime <- as.numeric(E1174_eTime)
E1174_dTime <- as.numeric(E1174_dTime)

#P-256
ecc_P256_time <- read.delim2("time_P-256.txt", header = FALSE, sep = ",")

colnames(ecc_P256_time) <- c("Encryption", "Decryption")

P256_eTime <- ecc_P256_time$Encryption
P256_dTime <- ecc_P256_time$Decryption

P256_eTime <- as.numeric(P256_eTime)
P256_dTime <- as.numeric(P256_dTime)

#M-221
ecc_M221_time <- read.delim2("time_M-221.txt", header = FALSE, sep = ",")

colnames(ecc_M221_time) <- c("Encryption", "Decryption")

M221_eTime <- ecc_M221_time$Encryption
M221_dTime <- ecc_M221_time$Decryption

M221_eTime <- as.numeric(M221_eTime)
M221_dTime <- as.numeric(M221_dTime)

ECC_text_length <- c(446,1100,2142,2972,3686)


#Comparing encryption time
mixed_ECC_Time <- data.frame(c1 = E1174_eTime, 
                                c2 = P256_eTime,
                                c3 = M221_eTime,
                                c4 = ECC_text_length)

colnames(mixed_ECC_Time) <- c("E1174_encTime","P256_encTime", "M221_encTime", "ECC_Text_length")


library(ggplot2)

s <- ggplot(mixed_ECC_Time, aes(x = ECC_Text_length)) +
  geom_point(aes(y = E1174_eTime), size = 4, color = "green", shape=21, fill = "green") +
  geom_point(aes(y = P256_eTime), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  geom_point(aes(y = M221_eTime), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = E1174_eTime, color = "E1174_eTime", group = 1), size = 1) +
  geom_line(aes(y = P256_eTime, color = "P256_eTime", group = 1), size = 1) +
  geom_line(aes(y = M221_eTime, color = "M221_eTime", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "Time (ms)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
s+scale_color_manual(values=c("green","lightsalmon", "royalblue" )) + 
  theme(panel.background = element_rect(fill = "white"))



# # Create data frame to graph
# mixed_encTime <- data.frame(c1 = aes_eTime, 
#                             c2 = rsa_eTime,
#                             c3 = ecc_eTime)
# 
# colnames(mixed_encTime) <- c("AES_encTime","RSA_encTime","ECC_encTime")

#Graphics

#Comparing AES, RSA, ECC

##install.packages("ggplot2")
# library(ggplot2)
# 
# p <- ggplot(mixed_encTime, aes(x = factor(1:nrow(mixed_encTime)))) +
#   geom_point(aes(y = AES_encTime), size = 4, color = "black", shape=7) +
#   geom_point(aes(y = RSA_encTime), size = 4, color = "blue", shape=4) +
#   geom_point(aes(y = ECC_encTime), size = 4, color = "red", shape=1) +
#   geom_line(aes(y = AES_encTime, color = "AES_encTime", group = 1), size = 1) +
#   geom_line(aes(y = RSA_encTime, color = "RSA_encTime", group = 1)) +
#   geom_line(aes(y = ECC_encTime, color = "ECC_encTime", group = 1)) +
#   #scale_y_continuous(breaks=c(0,300,600,900,1200,1500,1800,2100,2400,2700,3000,3300,3600)) + 
#   labs(x = "Repetitions", y = "Time (ms)", color = "Algorithm") +
#   theme(
#     legend.title = element_blank(),
#     legend.position = c(0.85, 0.8),
#     #panel.background = element_rect(fill = "white"),
#     panel.grid.major = element_line(color = "gray", linetype = "dashed"),
#     #panel.grid.minor = element_blank(),
#     axis.line = element_line(color = "black")
#   )
# 
# # Use custom color palettes
# p+scale_color_manual(values=c("black", "red", "blue")) + 
#   theme(panel.background = element_rect(fill = "white"))
# # Use brewer color palettes
# p+scale_color_brewer(palette="Dark2")
# # Use grey scale
# p + scale_color_grey()



#///////////////////////////////////THROUGHPUT///////////////////////////////////////////

#AES algortihm
setwd("C:/Users/User/Desktop/Cifradores/AES/AES_modificado")
#setwd("C:/Users/User/Desktop/Cifradores/RSA")
#setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#ECB
aes_ecb_throughput <- read.delim2("throughput_ECB.txt", header = FALSE, sep = ",")

colnames(aes_ecb_throughput) <- c("Operation", "InputSize (Bytes)", "ExecutionTime (milliseconds)", "ThroughputBps (Bps)", "Throughputbps (bps)")

aes_ecb_throughput <- aes_ecb_throughput[aes_ecb_throughput$Operation == "encrypt", ]

ecb_Through <- aes_ecb_throughput$`ThroughputBps (Bps)`

ecb_Through <- as.numeric(ecb_Through)

#CBC
aes_cbc_throughput <- read.delim2("throughput_CBC.txt", header = FALSE, sep = ",")

colnames(aes_cbc_throughput) <- c("Operation", "InputSize (Bytes)", "ExecutionTime (milliseconds)", "ThroughputBps (Bps)", "Throughputbps (bps)")

aes_cbc_throughput <- aes_cbc_throughput[aes_cbc_throughput$Operation == "encrypt", ]

cbc_Through <- aes_cbc_throughput$`ThroughputBps (Bps)`

cbc_Through <- as.numeric(cbc_Through)

text_length <- c(446,1100,2142,2972,3686)

#Comparing ECB vs CBC modes
mixed_aesmodes_Throughput <- data.frame(c1 = ecb_Through, 
                                  c2 = cbc_Through,
                                  c3 = text_length)

colnames(mixed_aesmodes_Throughput) <- c("ECB_Throughput","CBC_Throughput", "Text_length")

library(ggplot2)

p1 <- ggplot(mixed_aesmodes_Throughput, aes(x = Text_length)) +
  geom_point(aes(y = ECB_Throughput), size = 4, color = "darkgoldenrod", shape=7) +
  geom_point(aes(y = CBC_Throughput), size = 4, color = "deeppink", shape=1) +
  geom_line(aes(y = ECB_Throughput, color = "ECB_Throughput", group = 1), linewidth = 1) +
  geom_line(aes(y = CBC_Throughput, color = "CBC_Throughput", group = 1), linewidth = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "Throughput (Bps)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.9),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
p1 + scale_color_manual(values=c("deeppink", "darkgoldenrod")) + 
  theme(panel.background = element_rect(fill = "white"))



#RSA algorithm

#1024 bits
setwd("C:/Users/User/Desktop/Cifradores/RSA")

rsa_1024_throughput <- read.delim2("throughput_1024.txt", header = FALSE, sep = ",")

colnames(rsa_1024_throughput) <- c("Operation", "InputSize (Bytes)", "ThroughputBps (Bps)", "Throughputbps (bps)")

rsa_1024_throughput <- rsa_1024_throughput[rsa_1024_throughput$Operation == "encrypt", ]

RSA1024_throughput <- rsa_1024_throughput$`ThroughputBps (Bps)`

RSA1024_throughput <- as.numeric(RSA1024_throughput)


#2048 bits
rsa_2048_throughput <- read.delim2("throughput_2048.txt", header = FALSE, sep = ",")

colnames(rsa_2048_throughput) <- c("Operation", "InputSize (Bytes)", "ThroughputBps (Bps)", "Throughputbps (bps)")

rsa_2048_throughput <- rsa_2048_throughput[rsa_2048_throughput$Operation == "encrypt", ]

RSA2048_throughput <- rsa_2048_throughput$`ThroughputBps (Bps)`

RSA2048_throughput <- as.numeric(RSA2048_throughput)

#3072 bits
rsa_3072_throughput <- read.delim2("throughput_3072.txt", header = FALSE, sep = ",")

colnames(rsa_3072_throughput) <- c("Operation", "InputSize (Bytes)", "ThroughputBps (Bps)", "Throughputbps (bps)")

rsa_3072_throughput <- rsa_3072_throughput[rsa_3072_throughput$Operation == "encrypt", ]

RSA3072_throughput <- rsa_3072_throughput$`ThroughputBps (Bps)`

RSA3072_throughput <- as.numeric(RSA3072_throughput)

text_length_rsa <- c(99, 242, 354)

#Comparing ECB vs CBC modes
mixed_rsamodes_Throughput <- data.frame(c1 = RSA1024_throughput, 
                                        c2 = RSA2048_throughput,
                                        c3 = RSA3072_throughput)

colnames(mixed_rsamodes_Throughput) <- c("RSA_1024_Thput","RSA_2048_Thput", "RSA_3072_Thput")

library(ggplot2)

q1 <- ggplot(mixed_rsamodes_Throughput, aes(x = factor(1:nrow(mixed_rsamodes_Throughput)))) +
  geom_point(aes(y = RSA_1024_Thput), size = 4, color = "seagreen", shape=11) +
  geom_point(aes(y = RSA_2048_Thput), size = 4, color = "orangered", shape=5) +
  geom_point(aes(y = RSA_3072_Thput), size = 4, color = "mediumpurple", shape=0) +
  geom_line(aes(y = RSA_1024_Thput, color = "RSA_1024_Thput", group = 1), size = 1) +
  geom_line(aes(y = RSA_2048_Thput, color = "RSA_2048_Thput", group = 1), size = 1) +
  geom_line(aes(y = RSA_3072_Thput, color = "RSA_3072_Thput", group = 1), size = 1) +
  #scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Executions", y = "Throughput (Bps)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.85, 0.6),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
q1 + scale_color_manual(values=c("seagreen", "orangered", "mediumpurple")) + 
  theme(panel.background = element_rect(fill = "white"))



#ECC
setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#E-1174
ecc_E1174_throughput <- read.delim2("throughput_E-1174.txt", header = FALSE, sep = ",")

colnames(ecc_E1174_throughput) <- c("Operation", "InputSize", "ThroughputBps", "Throughputbps")

ecc_E1174_throughput <- ecc_E1174_throughput[ecc_E1174_throughput$Operation == "encrypt", ]

E1174_Thput <- ecc_E1174_throughput$ThroughputBps

E1174_Thput <- as.numeric(E1174_Thput)

#M-221
ecc_M221_throughput <- read.delim2("throughput_M-221.txt", header = FALSE, sep = ",")

colnames(ecc_M221_throughput) <- c("Operation", "InputSize", "ThroughputBps", "Throughputbps")

ecc_M221_throughput <- ecc_M221_throughput[ecc_M221_throughput$Operation == "encrypt", ]

M221_Thput <- ecc_M221_throughput$ThroughputBps

M221_Thput <- as.numeric(M221_Thput)


#P-256
ecc_P256_throughput <- read.delim2("throughput_P-256.txt", header = FALSE, sep = ",")

colnames(ecc_P256_throughput) <- c("Operation", "InputSize", "ThroughputBps", "Throughputbps")

ecc_P256_throughput <- ecc_P256_throughput[ecc_P256_throughput$Operation == "encrypt", ]

P256_Thput <- ecc_P256_throughput$ThroughputBps

P256_Thput <- as.numeric(P256_Thput)

ECC_text_length <- c(446,1100,2142,2972,3686)


#Comparing encryption time
mixed_ECC_Throughput <- data.frame(c1 = E1174_Thput, 
                             c2 = M221_Thput,
                             c3 = P256_Thput,
                             c4 = ECC_text_length)

colnames(mixed_ECC_Throughput) <- c("E1174_Thput","M221_Thput", "P256_Thput", "ECC_Text_length")

library(ggplot2)

s1 <- ggplot(mixed_ECC_Throughput, aes(x = ECC_Text_length)) +
  geom_point(aes(y = E1174_Thput), size = 4, color = "green", shape=21, fill = "green") +
  geom_point(aes(y = P256_Thput), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  geom_point(aes(y = M221_Thput), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = E1174_Thput, color = "E1174_Thput", group = 1), size = 1) +
  geom_line(aes(y = P256_Thput, color = "P256_Thput", group = 1), size = 1) +
  geom_line(aes(y = M221_Thput, color = "M221_Thput", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "Throoughput (Bps)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
s1 + scale_color_manual(values=c("green","lightsalmon", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))



#/////////////////////MEMORY USAGE/////////////////////////

#AES
setwd("C:/Users/User/Desktop/Cifradores/AES/AES_modificado")

#ECB
aes_ecb_memory <- read.delim2("memory_ECB.txt", header = FALSE, sep = ",")

colnames(aes_ecb_memory) <- c("Operation", "InputSize", "MemoryUsage")

aes_ecb_memory <- aes_ecb_memory[aes_ecb_memory$Operation == "encrypt", ]

ecb_memory <- aes_ecb_memory$MemoryUsage

ecb_memory <- as.numeric(ecb_memory)

#CBC
aes_cbc_memory <- read.delim2("memory_CBC.txt", header = FALSE, sep = ",")

colnames(aes_cbc_memory) <- c("Operation", "InputSize", "MemoryUsage")

aes_cbc_memory <- aes_cbc_memory[aes_cbc_memory$Operation == "encrypt", ]

cbc_memory <- aes_cbc_memory$MemoryUsage

cbc_memory <- as.numeric(cbc_memory)

text_length <- c(446,1100,2142,2972,3686)

#Comparing ECB vs CBC modes
mixed_aesmodes_Memory <- data.frame(c1 = ecb_memory, 
                                    c2 = cbc_memory,
                                    c3 = text_length)

colnames(mixed_aesmodes_Memory) <- c("ECB_Memory","CBC_Memory", "Text_length")

library(ggplot2)

p2 <- ggplot(mixed_aesmodes_Memory, aes(x = Text_length)) +
  geom_point(aes(y = ECB_Memory), size = 4, color = "darkgoldenrod", shape=7) +
  geom_point(aes(y = CBC_Memory), size = 4, color = "deeppink", shape=1) +
  geom_line(aes(y = ECB_Memory, color = "ECB_Memory", group = 1), linewidth = 1) +
  geom_line(aes(y = CBC_Memory, color = "CBC_Memory", group = 1), linewidth = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (Bytes)", y = "Memory (Bytes)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.9),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
p2 + scale_color_manual(values=c("deeppink", "darkgoldenrod")) + 
  theme(panel.background = element_rect(fill = "white"))


#RSA algorithm

#1024 bits
setwd("C:/Users/User/Desktop/Cifradores/RSA")

rsa_1024_memory <- read.delim2("memory_1024.txt", header = FALSE, sep = ",")

colnames(rsa_1024_memory) <- c("Operation", "InputSize (Bytes)", "MemoryUsage (Bytes)")

rsa_1024_memory <- rsa_1024_memory[rsa_1024_memory$Operation == "encrypt", ]

RSA1024_memory <- rsa_1024_memory$`MemoryUsage (Bytes)`

RSA1024_memory <- as.numeric(RSA1024_memory)


#2048 bits
rsa_2048_memory <- read.delim2("memory_2048.txt", header = FALSE, sep = ",")

colnames(rsa_2048_memory) <- c("Operation", "InputSize (Bytes)", "MemoryUsage (Bytes)")

rsa_2048_memory <- rsa_2048_memory[rsa_2048_memory$Operation == "encrypt", ]

RSA2048_memory <- rsa_2048_memory$`MemoryUsage (Bytes)`

RSA2048_memory <- as.numeric(RSA2048_memory)

#3072 bits
rsa_3072_memory <- read.delim2("memory_3072.txt", header = FALSE, sep = ",")

colnames(rsa_3072_memory) <- c("Operation", "InputSize (Bytes)", "MemoryUsage (Bytes)")

rsa_3072_memory <- rsa_3072_memory[rsa_3072_memory$Operation == "encrypt", ]

RSA3072_memory <- rsa_3072_memory$`MemoryUsage (Bytes)`

RSA3072_memory <- as.numeric(RSA3072_memory)

text_length_rsa <- c(99, 242, 354)

#Comparing ECB vs CBC modes
mixed_rsamodes_Memory <- data.frame(c1 = RSA1024_memory, 
                                        c2 = RSA2048_memory,
                                        c3 = RSA3072_memory)

colnames(mixed_rsamodes_Memory) <- c("RSA_1024_Memory","RSA_2048_Memory", "RSA_3072_Memory")

library(ggplot2)

q2 <- ggplot(mixed_rsamodes_Memory, aes(x = factor(1:nrow(mixed_rsamodes_Memory)))) +
  geom_point(aes(y = RSA_1024_Memory), size = 4, color = "seagreen", shape=11) +
  geom_point(aes(y = RSA_2048_Memory), size = 4, color = "orangered", shape=5) +
  geom_point(aes(y = RSA_3072_Memory), size = 4, color = "mediumpurple", shape=0) +
  geom_line(aes(y = RSA_1024_Memory, color = "RSA_1024_Memory", group = 1), size = 1) +
  geom_line(aes(y = RSA_2048_Memory, color = "RSA_2048_Memory", group = 1), size = 1) +
  geom_line(aes(y = RSA_3072_Memory, color = "RSA_3072_Memory", group = 1), size = 1) +
  #scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Executions", y = "Memory (Bytes)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.85, 0.6),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
q2 + scale_color_manual(values=c("seagreen", "orangered", "mediumpurple")) + 
  theme(panel.background = element_rect(fill = "white"))


#ECC
setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#E-1174
ecc_E1174_memory <- read.delim2("memory_E-1174.txt", header = FALSE, sep = ",")

colnames(ecc_E1174_memory) <- c("Operation", "InputSize", "MemoryUsage")

ecc_E1174_memory <- ecc_E1174_memory[ecc_E1174_memory$Operation == "encrypt", ]

E1174_mem <- ecc_E1174_memory$MemoryUsage

E1174_mem <- as.numeric(E1174_mem)

#M-221
ecc_M221_memory <- read.delim2("memory_M-221.txt", header = FALSE, sep = ",")

colnames(ecc_M221_memory) <- c("Operation", "InputSize", "MemoryUsage")

ecc_M221_memory <- ecc_M221_memory[ecc_M221_memory$Operation == "encrypt", ]

M221_mem <- ecc_M221_memory$MemoryUsage

M221_mem <- as.numeric(M221_mem)


#P-256
ecc_P256_memory <- read.delim2("memory_P-256.txt", header = FALSE, sep = ",")

colnames(ecc_P256_memory) <- c("Operation", "InputSize", "MemoryUsage")

ecc_P256_memory <- ecc_P256_memory[ecc_P256_memory$Operation == "encrypt", ]

P256_mem <- ecc_P256_memory$MemoryUsage

P256_mem <- as.numeric(P256_mem)

ECC_text_length <- c(446,1100,2142,2972,3686)


#Comparing encryption time
mixed_ECC_memory <- data.frame(c1 = E1174_mem, 
                                   c2 = M221_mem,
                                   c3 = P256_mem,
                                   c4 = ECC_text_length)

colnames(mixed_ECC_memory) <- c("E1174_Memory","M221_Memory", "P256_Memory", "ECC_Text_length")

library(ggplot2)

s2 <- ggplot(mixed_ECC_memory, aes(x = ECC_Text_length)) +
  geom_point(aes(y = E1174_Memory), size = 4, color = "green", shape=21, fill = "green") +
  geom_point(aes(y = P256_Memory), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  geom_point(aes(y = M221_Memory), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = E1174_Memory, color = "E1174_Memory", group = 1), size = 1) +
  geom_line(aes(y = P256_Memory, color = "P256_Memory", group = 1), size = 1) +
  geom_line(aes(y = M221_Memory, color = "M221_Memory", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "Memory (Bytes)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
s2 + scale_color_manual(values=c("green","lightsalmon", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))


#/////////////////////CPU CONSUMPTION//////////////////////////

#AES
setwd("C:/Users/User/Desktop/Cifradores/AES")

#ECB
aes_ecb_cpu <- read.delim2("CPU_ECB.txt", header = FALSE, sep = ",")

colnames(aes_ecb_cpu) <- c("Operation", "InputSize", "Time", "ThrougByte", "Throughbits", "CPU")

aes_ecb_cpu <- aes_ecb_cpu[aes_ecb_cpu$Operation == "encrypt", ]

ecb_cpu <- aes_ecb_cpu$CPU

ecb_cpu <- as.numeric(ecb_cpu)

#CBC
aes_cbc_cpu <- read.delim2("CPU_CBC.txt", header = FALSE, sep = ",")

colnames(aes_cbc_cpu) <- c("Operation", "InputSize", "Time", "ThrougByte", "Throughbits", "CPU")

aes_cbc_cpu <- aes_cbc_cpu[aes_cbc_cpu$Operation == "encrypt", ]

cbc_cpu <- aes_cbc_cpu$CPU

cbc_cpu <- as.numeric(cbc_cpu)

text_length <- c(446,1100,2142,2972,3686)

#Comparing ECB vs CBC modes
mixed_aesmodes_CPU <- data.frame(c1 = ecb_cpu, 
                                    c2 = cbc_cpu,
                                    c3 = text_length)

colnames(mixed_aesmodes_CPU) <- c("ECB_CPU","CBC_CPU", "Text_length")

library(ggplot2)

p2 <- ggplot(mixed_aesmodes_CPU, aes(x = Text_length)) +
  geom_point(aes(y = ECB_CPU), size = 4, color = "darkgoldenrod", shape=7) +
  geom_point(aes(y = CBC_CPU), size = 4, color = "deeppink", shape=1) +
  geom_line(aes(y = ECB_CPU, color = "ECB_CPU", group = 1), linewidth = 1) +
  geom_line(aes(y = CBC_CPU, color = "CBC_CPU", group = 1), linewidth = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (Bytes)", y = "CPU Consumption (%)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.8),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
p2 + scale_color_manual(values=c("deeppink", "darkgoldenrod")) + 
  theme(panel.background = element_rect(fill = "white"))


#RSA algorithm

#1024 bits
setwd("C:/Users/User/Desktop/Cifradores/RSA")

rsa_1024_cpu <- read.delim2("CPU_1024.txt", header = FALSE, sep = ",")

colnames(rsa_1024_cpu) <- c("Operation", "InputSize", "Time", "ThrougByte", "Throughbits", "CPU")

rsa_1024_cpu <- rsa_1024_cpu[rsa_1024_cpu$Operation == "encrypt", ]

RSA1024_cpu <- rsa_1024_cpu$CPU

RSA1024_cpu <- as.numeric(RSA1024_cpu)


#2048 bits
rsa_2048_cpu <- read.delim2("CPU_2048.txt", header = FALSE, sep = ",")

colnames(rsa_2048_cpu) <- c("Operation", "InputSize", "Time", "ThrougByte", "Throughbits", "CPU")

rsa_2048_cpu <- rsa_2048_cpu[rsa_2048_cpu$Operation == "encrypt", ]

RSA2048_cpu <- rsa_2048_cpu$CPU

RSA2048_cpu <- as.numeric(RSA2048_cpu)

#3072 bits
rsa_3072_cpu <- read.delim2("CPU_3072.txt", header = FALSE, sep = ",")

colnames(rsa_3072_cpu) <- c("Operation", "InputSize", "Time", "ThrougByte", "Throughbits", "CPU")

rsa_3072_cpu <- rsa_3072_cpu[rsa_3072_cpu$Operation == "encrypt", ]

RSA3072_cpu <- rsa_3072_cpu$CPU

RSA3072_cpu <- as.numeric(RSA3072_cpu)

text_length_rsa <- c(99, 242, 354)

#Comparing ECB vs CBC modes
mixed_rsamodes_CPU <- data.frame(c1 = RSA1024_cpu, 
                                    c2 = RSA2048_cpu,
                                    c3 = RSA3072_cpu)

colnames(mixed_rsamodes_CPU) <- c("RSA_1024_CPU","RSA_2048_CPU", "RSA_3072_CPU")

library(ggplot2)

q2 <- ggplot(mixed_rsamodes_CPU, aes(x = factor(1:nrow(mixed_rsamodes_CPU)))) +
  geom_point(aes(y = RSA_1024_CPU), size = 4, color = "seagreen", shape=11) +
  geom_point(aes(y = RSA_2048_CPU), size = 4, color = "orangered", shape=5) +
  geom_point(aes(y = RSA_3072_CPU), size = 4, color = "mediumpurple", shape=0) +
  geom_line(aes(y = RSA_1024_CPU, color = "RSA_1024_CPU", group = 1), size = 1) +
  geom_line(aes(y = RSA_2048_CPU, color = "RSA_2048_CPU", group = 1), size = 1) +
  geom_line(aes(y = RSA_3072_CPU, color = "RSA_3072_CPU", group = 1), size = 1) +
  #scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Executions", y = "CPU Consumption (%)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.85, 0.4),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
q2 + scale_color_manual(values=c("seagreen", "orangered", "mediumpurple")) + 
  theme(panel.background = element_rect(fill = "white"))


#ECC
setwd("C:/Users/User/Desktop/Cifradores/ECC - final")

#E-1174
ecc_E1174_cpu <- read.delim2("CPU_E-1174.txt", header = FALSE, sep = ",")

colnames(ecc_E1174_cpu) <- c("Operation", "InputSize", "ThrougByte", "Throughbits", "CPU")

ecc_E1174_cpu <- ecc_E1174_cpu[ecc_E1174_cpu$Operation == "encrypt", ]

E1174_cpu <- ecc_E1174_cpu$CPU

E1174_cpu <- as.numeric(E1174_cpu)

#M-221
ecc_M221_cpu <- read.delim2("CPU_M-221.txt", header = FALSE, sep = ",")

colnames(ecc_M221_cpu) <- c("Operation", "InputSize", "ThrougByte", "Throughbits", "CPU")

ecc_M221_cpu <- ecc_M221_cpu[ecc_M221_cpu$Operation == "encrypt", ]

M221_cpu <- ecc_M221_cpu$CPU

M221_cpu <- as.numeric(M221_cpu)


#P-256
ecc_P256_cpu <- read.delim2("CPU_P-256.txt", header = FALSE, sep = ",")

colnames(ecc_P256_cpu) <- c("Operation", "InputSize", "ThrougByte", "Throughbits", "CPU")

ecc_P256_cpu <- ecc_P256_cpu[ecc_P256_cpu$Operation == "encrypt", ]

P256_cpu <- ecc_P256_cpu$CPU

P256_cpu <- as.numeric(P256_cpu)

ECC_text_length <- c(446,1100,2142,2972,3686)


#Comparing encryption time
mixed_ECC_cpu <- data.frame(c1 = E1174_cpu, 
                               c2 = M221_cpu,
                               c3 = P256_cpu,
                               c4 = ECC_text_length)

colnames(mixed_ECC_cpu) <- c("E1174_CPU","M221_CPU", "P256_CPU", "ECC_Text_length")

library(ggplot2)

s2 <- ggplot(mixed_ECC_cpu, aes(x = ECC_Text_length)) +
  geom_point(aes(y = E1174_CPU), size = 4, color = "green", shape=21, fill = "green") +
  geom_point(aes(y = P256_CPU), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  geom_point(aes(y = M221_CPU), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = E1174_CPU, color = "E1174_CPU", group = 1), size = 1) +
  geom_line(aes(y = P256_CPU, color = "P256_CPU", group = 1), size = 1) +
  geom_line(aes(y = M221_CPU, color = "M221_CPU", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  labs(x = "Text size (bytes)", y = "CPU Consumption (%)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
s2 + scale_color_manual(values=c("green","lightsalmon", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))






#///////////////GLOBAL COMPARISON///////////////////////////////////////

colnames(data) <- c("Operation", "InputSize", "MemoryUsage")

library(ggplot2)

ggplot(data, aes(x = InputSize, y = MemoryUsage, color = Operation)) +
  geom_point() +
  labs(x = "Input Size (bytes)", y = "Memory Usage (bytes)", title = "RSA Memory Usage") +
  theme_minimal()

summary_stats <- data %>%
  group_by(Operation) %>%
  summarise(AvgMemoryUsage = mean(MemoryUsage),
            MinMemoryUsage = min(MemoryUsage),
            MaxMemoryUsage = max(MemoryUsage))
print(summary_stats)

#//////////////////////////////////////////////////////////////////////////////////
# Time comparison
text_length <- c(446,1100,2142,2972,3686)


time_comparison <- data.frame(c1 = cbc_eTime, 
                               c2 = P256_eTime,
                               c4 = text_length)

colnames(time_comparison) <- c("CBC_Time", "P256_Time", "Text_Size")

library(ggplot2)

c1 <- ggplot(time_comparison, aes(x = Text_Size)) +
  geom_point(aes(y = cbc_eTime), size = 4, color = "green", shape=21, fill = "green") +
  geom_point(aes(y = P256_eTime), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  #geom_point(aes(y = rsa1024_Time), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = cbc_eTime, color = "cbc_eTime", group = 1), size = 1) +
  geom_line(aes(y = P256_eTime, color = "P256_eTime", group = 1), size = 1) +
  #geom_line(aes(y = rsa1024_Time, color = "rsa1024_Time", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  #scale_y_continuous(breaks=c(0,20, 40, 60, 80, 100,2000,5000,7500,8000)) +
  labs(x = "Text size (bytes)", y = "Time (ms)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
c1 + scale_color_manual(values=c("green", "royalblue", "lightsalmon")) + 
  theme(panel.background = element_rect(fill = "white"))



#Throughput comparison

colnames(data) <- c("Operation", "InputSize", "MemoryUsage")

library(ggplot2)

ggplot(data, aes(x = InputSize, y = MemoryUsage, color = Operation)) +
  geom_point() +
  labs(x = "Input Size (bytes)", y = "Memory Usage (bytes)", title = "RSA Memory Usage") +
  theme_minimal()

summary_stats <- data %>%
  group_by(Operation) %>%
  summarise(AvgMemoryUsage = mean(MemoryUsage),
            MinMemoryUsage = min(MemoryUsage),
            MaxMemoryUsage = max(MemoryUsage))
print(summary_stats)

#//////////////////////////////////////////////////////////////////////////////////
# Throughput comparison
text_length <- c(446,1100,2142,2972,3686)


throughput_comparison <- data.frame(c1 = cbc_Through, 
                              c2 = P256_Thput,
                              c3 = text_length)

colnames(throughput_comparison) <- c("CBC_Throughput", "P256_Throughput", "Text_Size")

library(ggplot2)

c2 <- ggplot(throughput_comparison, aes(x = Text_Size)) +
  geom_point(aes(y = cbc_Through), size = 4, color = "green", shape=20, fill = "green") +
  geom_point(aes(y = P256_Thput), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  #geom_point(aes(y = rsa1024_Time), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = cbc_Through, color = "cbc_Through", group = 1), size = 1) +
  geom_line(aes(y = P256_Thput, color = "P256_Thput", group = 1), size = 1) +
  #geom_line(aes(y = rsa1024_Time, color = "rsa1024_Time", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  #scale_y_continuous(breaks=c(0,20, 40, 60, 80, 100,2000,5000,7500,8000)) +
  labs(x = "Text size (bytes)", y = "Throughput (bytes)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
c2 + scale_color_manual(values=c("green", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))



#//////////////////////////////////////////////////////////////////////////////////

#Memory comparison
text_length <- c(446,1100,2142,2972,3686)


memory_comparison <- data.frame(c1 = ecb_memory, 
                                    c2 = P256_mem,
                                    c3 = text_length)

colnames(memory_comparison) <- c("ECB_Memory", "P256_Memory", "Text_Size")

library(ggplot2)

c3 <- ggplot(memory_comparison, aes(x = Text_Size)) +
  geom_point(aes(y = ecb_memory), size = 4, color = "lightpink", shape=19, fill = "lightpink") +
  geom_point(aes(y = P256_mem), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  #geom_point(aes(y = rsa1024_Time), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = ecb_memory, color = "ecb_memory", group = 1), size = 1) +
  geom_line(aes(y = P256_mem, color = "P256_mem", group = 1), size = 1) +
  #geom_line(aes(y = rsa1024_Time, color = "rsa1024_Time", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  #scale_y_continuous(breaks=c(0,20, 40, 60, 80, 100,2000,5000,7500,8000)) +
  labs(x = "Text size (bytes)", y = "Memory (bytes)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
c3 + scale_color_manual(values=c("lightpink", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))




#////////////////////////////////////////////////////////////////////////////////

#CPU Consumption comparison

text_length <- c(446,1100,2142,2972,3686)


cpu_comparison <- data.frame(c1 = ecb_cpu, 
                                c2 = M221_cpu,
                                c3 = text_length)

colnames(cpu_comparison) <- c("ECB_CPU", "M221_CPU", "Text_Size")

library(ggplot2)

c4 <- ggplot(cpu_comparison, aes(x = Text_Size)) +
  geom_point(aes(y = ecb_cpu), size = 4, color = "lightpink", shape=19, fill = "lightpink") +
  geom_point(aes(y = M221_cpu), size = 4, color = "royalblue", shape=22, fill = "royalblue") +
  #geom_point(aes(y = rsa1024_Time), size = 4, color = "lightsalmon", shape=23, fill = "lightsalmon") +
  geom_line(aes(y = ecb_cpu, color = "ecb_cpu", group = 1), size = 1) +
  geom_line(aes(y = M221_cpu, color = "M221_cpu", group = 1), size = 1) +
  #geom_line(aes(y = rsa1024_Time, color = "rsa1024_Time", group = 1), size = 1) +
  scale_x_continuous(breaks=c(0,446,1100,2142,2972,3686)) + 
  #scale_y_continuous(breaks=c(0,20, 40, 60, 80, 100,2000,5000,7500,8000)) +
  labs(x = "Text size (bytes)", y = "CPU Consumption (%)", color = "Algorithm") +
  theme(
    legend.title = element_blank(),
    legend.position = c(0.15, 0.85),
    #panel.background = element_rect(fill = "white"),
    panel.grid.major = element_line(color = "gray", linetype = "dashed"),
    #panel.grid.minor = element_blank(),
    axis.line = element_line(color = "black"),
    panel.border = element_rect(color = "black", fill = NA, linewidth = 0.5),
    legend.background = element_rect(fill = "white"),
    legend.box.background = element_rect(color = "black", linewidth = 0.5),
    legend.box.margin = margin(1, 1, 1, 1)
  )

# Use custom color palettes
c4 + scale_color_manual(values=c("lightpink", "royalblue")) + 
  theme(panel.background = element_rect(fill = "white"))

