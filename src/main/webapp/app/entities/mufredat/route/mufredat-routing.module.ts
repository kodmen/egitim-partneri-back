import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MufredatComponent } from '../list/mufredat.component';
import { MufredatDetailComponent } from '../detail/mufredat-detail.component';
import { MufredatUpdateComponent } from '../update/mufredat-update.component';
import { MufredatRoutingResolveService } from './mufredat-routing-resolve.service';

const mufredatRoute: Routes = [
  {
    path: '',
    component: MufredatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MufredatDetailComponent,
    resolve: {
      mufredat: MufredatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MufredatUpdateComponent,
    resolve: {
      mufredat: MufredatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MufredatUpdateComponent,
    resolve: {
      mufredat: MufredatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mufredatRoute)],
  exports: [RouterModule],
})
export class MufredatRoutingModule {}
