import React, { FC, HTMLAttributes, ReactNode } from "react";
import { BlurOptions, ModalOptions } from "../components/Wrappers";

export type WithBlur = (Component: ReactNode | any, blurOptions?: BlurOptions) => FC<React.DetailedHTMLProps<HTMLAttributes<HTMLElement>, HTMLElement>>
export type WithModal = (Component: ReactNode | any, modalOptions?: ModalOptions) => FC<React.DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>>